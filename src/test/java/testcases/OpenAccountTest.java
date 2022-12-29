package testcases;

import base.BaseTest;
import org.testng.annotations.Test;
import util.Constants;
import util.DataProviders;
import util.DataUtil;
import util.ExcelReader;

import java.util.HashMap;

public class OpenAccountTest extends BaseTest {

    @Test(dataProviderClass = DataProviders.class, dataProvider = "bankManagerDP")
    public void openAccountTest(HashMap<String, String> data) {
        ExcelReader excelReader = new ExcelReader(Constants.MANAGER_SUITE_EXCEL_PATH);
        DataUtil.checkExecution("BankManagerSuite", "OpenAccountTest", data.get("Runmode"), excelReader);
        browser = getBrowser(data.get("browser"));
        br.set(browser);
        navigate(Constants.BASE_URL);

        click("bank_manager_login_button");
        click("open_account_button");

        select("customer_name_drop-down", data.get("customer"));
        select("currency_drop-down", data.get("currency"));

        click("process_button");
    }
}
