package testcases;

import base.BaseTest;
import org.testng.annotations.Test;
import util.Constants;
import util.DataProviders;
import util.DataUtil;
import util.ExcelReader;

import java.util.HashMap;

public class AddCustomerTest extends BaseTest {

    @Test(dataProviderClass = DataProviders.class, dataProvider = "bankManagerDP")
    public void addCustomerTest(HashMap<String,String> data) {

        // add comments
        ExcelReader excelReader = new ExcelReader(Constants.MANAGER_SUITE_EXCEL_PATH);
        DataUtil.checkExecution("BankManagerSuite", "AddCustomerTest", data.get("Runmode"), excelReader);
        browser = getBrowser(data.get("browser"));
        br.set(browser);
        navigate(Constants.BASE_URL);

        click("bank_manager_login_button");
        click("add_customer_button");
        type("first_name_field", data.get("firstname"));
        type("last_name_field", data.get("lastname"));
        type("post_code_field", data.get("postcode"));
        click("submit_new_customer_button");
    }
}
