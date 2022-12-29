package util;

import org.testng.SkipException;
import org.testng.annotations.DataProvider;

import java.util.HashMap;

public class DataUtil {

    public static void checkExecution(String testSuiteName,
                                      String testCaseName,
                                      String dataRunMode,
                                      ExcelReader excel) {
        if(!isSuiteRunnable(testSuiteName)) {
            throw new SkipException("Skipping the test: " + testCaseName +
                    " as the Runmode of test Suite: " + testSuiteName + " is NO");
        }
        if(!isTestRunnable(testCaseName, excel)) {
            throw new SkipException("Skipping the test: " + testCaseName +
                    " as the Runmode of test Suite: " + testCaseName + " is NO");
        }
        if(dataRunMode.equalsIgnoreCase(Constants.RUNMODE_NO)) {
            throw new SkipException("Skipping the test: " + testCaseName +
                    " as the Runmode of Data set to NO");
        }
    }

    @DataProvider
    public static Object[][] getData(String testCase, ExcelReader excelReader) {
        int rowCount = excelReader.getRowCount(Constants.DATA_SHEET);
        System.out.println("Total count of rows: " + rowCount);

        String testName = testCase;

        // Find the start of test-case
        int testCaseNameRowNum = 1;

        for(testCaseNameRowNum = 1; testCaseNameRowNum<=rowCount; testCaseNameRowNum++) {
            String testCaseName = excelReader.getCellData(Constants.DATA_SHEET, 0, testCaseNameRowNum);

            if(testCaseName.equalsIgnoreCase(testName))
                break;
        }

        System.out.println("Test case starts from row number: " + testCaseNameRowNum);

        // Checking total rows of data in test case
        int dataStartRowNum = testCaseNameRowNum + 2;
        int totalNumDataRaws = 0;
        while(!excelReader.getCellData(Constants.DATA_SHEET, 0, dataStartRowNum + totalNumDataRaws).equals(""))
            totalNumDataRaws++;

        System.out.println("Total rows of data: " + totalNumDataRaws);

        // Checking the total number of columns in test case
        int titleRowNum = testCaseNameRowNum + 1;
        int totalTestCaseColumnNum = 0;

        while(!excelReader.getCellData(Constants.DATA_SHEET, totalTestCaseColumnNum, titleRowNum).equals(""))
            totalTestCaseColumnNum++;

        System.out.println("The total number of column in test case: " + totalTestCaseColumnNum);

        // Print data
        Object[][] data = new Object[totalNumDataRaws][1];
        for(int rNum=dataStartRowNum; rNum<(dataStartRowNum+totalNumDataRaws); rNum++) {
            HashMap<String,String> table = new HashMap<String, String>();
            for (int cNum = 0; cNum < totalTestCaseColumnNum; cNum++) {
                String testData = excelReader.getCellData(Constants.DATA_SHEET, cNum, rNum);
                String columnName = excelReader.getCellData(Constants.DATA_SHEET, cNum, titleRowNum);

                table.put(columnName, testData);
            }
            data[rNum-dataStartRowNum][0] = table;
        }

        return data;
    }

    public static boolean isSuiteRunnable(String suiteName) {

        ExcelReader excel = new ExcelReader(Constants.SUITE_EXCEL_PATH);
        int rows = excel.getRowCount(Constants.SUITE_SHEET);

        for (int rowNum = 2; rowNum <= rows; rowNum++) {

            String data = excel.getCellData(Constants.SUITE_SHEET, Constants.SUITENAME_COL, rowNum);

            if (data.equals(suiteName)) {

                String runmode = excel.getCellData(Constants.SUITE_SHEET, Constants.RUNMODE_COL, rowNum);
                if (runmode.equals(Constants.RUNMODE_YES))
                    return true;
                else
                    return false;
            }
        }
        return false;
    }

    public static boolean isTestRunnable(String testCaseName, ExcelReader excel) {

        int rows = excel.getRowCount(Constants.TESTCASE_SHEET);

        for (int rowNum = 2; rowNum <= rows; rowNum++) {

            String data = excel.getCellData(Constants.TESTCASE_SHEET, Constants.TESTCASE_COL, rowNum);

            if (data.equalsIgnoreCase(testCaseName)) {
                String runmode = excel.getCellData(Constants.TESTCASE_SHEET, Constants.RUNMODE_COL, rowNum);
                if (runmode.equals(Constants.RUNMODE_YES))
                    return true;
                else
                    return false;
            }
        }
        return false;
    }
}
