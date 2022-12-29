package testcases;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import util.Constants;

public class BankManagerLoginTest extends BaseTest {

    @Test
    public void loginAsBankManager() {
        browser = getBrowser("chrome");
        br.set(browser);
        navigate(Constants.BASE_URL);

        click("bank_manager_login_button");
        waitForElementToDisappear("bank_manager_login_button");

        Assert.assertTrue(isElementPresent("add_customer_button"));
        Assert.assertTrue(isElementPresent("open_account_button"));
        Assert.assertTrue(isElementPresent("customers_button"));
    }
}
