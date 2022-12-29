package rough;

import base.BaseTest;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test
    public void typingSearchRequestInGoogle() {
        browser = getBrowser("chrome");
        br.set(browser);
        navigate("https://google.com");
        type("searchField", "Playwright is on Fire!!!");
    }

    @Test
    public void doGmailLogin() {
        browser = getBrowser("firefox");
        br.set(browser);
        navigate("https://gmail.com");
        type("username", "stalvio.neto@gmail.com");
    }
}
