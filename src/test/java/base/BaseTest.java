package base;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.WaitForSelectorState;
import listeners.ExtentListeners;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class BaseTest {

    private Playwright playwright;
    public Browser browser;
    public Page page;
    public static Properties OR = new Properties();
    //  public static Properties config = new Properties();
    private static FileInputStream fis;
    private Logger log = Logger.getLogger(this.getClass());

    private static ThreadLocal<Playwright> pw = new ThreadLocal<>();
    protected static ThreadLocal<Browser> br = new ThreadLocal<>();
    private static ThreadLocal<Page> pg = new ThreadLocal<>();

    public static Playwright getPlaywright() {
        return pw.get();
    }

    public static Browser getBrowser() {
        return br.get();
    }

    public static Page getPage() {
        return pg.get();
    }

    @BeforeSuite
    public void setUp() {
        PropertyConfigurator.configure("src/test/resources/log4j.properties");
        log.info("Test Execution started!!!");

        try {
            fis = new FileInputStream("src/test/resources/OR.properties");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            OR.load(fis);
            log.info("OR properties file loaded");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void click(String locatorKey) {
        try {
            getLocator(locatorKey).click();
            log.info("Clicking the Element: " + locatorKey);
            ExtentListeners.getExtent().info("Clicking the Element: " + locatorKey);
        } catch (Throwable t) {
            log.error("Error while clicking " + locatorKey);
            ExtentListeners.getExtent().fail("Error while clicking " + locatorKey);
            Assert.fail(t.getMessage());
        }
    }

    public void type(String locatorKey, String value) {
        try {
            getLocator(locatorKey).fill(value);
            log.info("Typing '" + value + "' in " + locatorKey);
            ExtentListeners.getExtent().info("Typing '" + value + "' in " + locatorKey);
        } catch (Throwable t) {
            log.error("Error while typing in " + locatorKey);
            Assert.fail(t.getMessage());
        }
    }

    public void select(String locatorKey, String optionName) {
        try {
            String locator = OR.getProperty(locatorKey);
            System.out.println();
            getPage().selectOption(locator, new SelectOption().setLabel(optionName));
            log.info("Selecting '" + optionName + "' in " + locatorKey);
            ExtentListeners.getExtent().info("Selecting '" + optionName + "' in " + locatorKey);
        } catch (Throwable t) {
            log.error("Error while selecting '" + optionName + "' in " + locatorKey);
            Assert.fail(t.getMessage());
        }
    }

    protected Locator getLocator(String locatorKey) {
        return getPage().locator(OR.getProperty(locatorKey));
    }

    public void waitForElementToDisappear(String locatorKey) {
        getLocator(locatorKey).waitFor(
                new Locator.WaitForOptions().setState(WaitForSelectorState.DETACHED).setTimeout(2000));
    }

    public Browser getBrowser(String browserName) {
        playwright = Playwright.create();
        pw.set(playwright);

        switch (browserName) {
            case "chrome":
                log.info("Launching Chrome browser");
                return playwright.chromium().launch(new BrowserType.LaunchOptions()
                        .setChannel("chrome")
                        .setHeadless(false));
            case "chromeHeadless":
                log.info("Launching headless Chrome browser");
                return playwright.chromium().launch(new BrowserType.LaunchOptions()
                        .setHeadless(true));
            case "firefox":
                log.info("Launching FireFox browser");
                return playwright.firefox().launch(new BrowserType.LaunchOptions()
                        .setChannel("firefox")
                        .setHeadless(false));
            case "webkit":
                log.info("Launching Webkit browser");
                return playwright.webkit().launch(new BrowserType.LaunchOptions()
                        .setHeadless(false));
            default:
                return playwright.chromium().launch(new BrowserType.LaunchOptions()
                        .setChannel("chrome")
                        .setHeadless(false));
        }
    }

    protected boolean isElementPresent(String locatorKey) {

        if(getLocator(locatorKey).isVisible()) {
                log.info("The element " + locatorKey + " is visible");
                ExtentListeners.getExtent().info("The element " + locatorKey + " is visible");
                return true;
            } else {
                log.info("The element " + locatorKey + " is not visible");
                ExtentListeners.getExtent().info("The element " + locatorKey + " is not visible");
                return false;
            }
    }

    public void navigate(String url) {
        page = getBrowser().newPage();
        pg.set(page);
        getPage().navigate(url);
        log.info("Navigated to: " + url);

        getPage().onDialog( dialog -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dialog.accept();
            System.out.println(dialog.message());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }

    @AfterMethod
    public void quit() {
        if (getPage() != null) {
            getPage().close();
            getBrowser().close();
        }
    }
    @AfterSuite
    public void quitPlaywright() {
        if (getPage() != null)
            getPlaywright().close();
    }
}
