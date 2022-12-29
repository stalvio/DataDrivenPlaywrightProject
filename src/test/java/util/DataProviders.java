package util;

import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;

public class DataProviders {

    @DataProvider(name = "bankManagerDP")
    public Object[][] getManagerDataSuite(Method m) {
        System.out.println(m.getName());

        ExcelReader excelReader = new ExcelReader(Constants.MANAGER_SUITE_EXCEL_PATH);
        String testCase = m.getName();

        return DataUtil.getData(testCase, excelReader);
    }

    @DataProvider(name = "customerDP")
    public Object[][] getCustomerDataSuite(Method m) {
        System.out.println(m.getName());

        ExcelReader excelReader = new ExcelReader(Constants.CUSTOMER_SUITE_EXCEL_PATH);
        String testCase = m.getName();

        return DataUtil.getData(testCase, excelReader);
    }
}
