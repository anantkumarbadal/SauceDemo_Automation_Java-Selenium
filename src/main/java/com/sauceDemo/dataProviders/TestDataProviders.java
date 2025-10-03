package com.sauceDemo.dataProviders;

import com.sauceDemo.utils.ExcelUtils;
import org.testng.annotations.DataProvider;

import java.io.IOException;

public class TestDataProviders {

    /*
    private static final String LOGIN_EXCEL_FILE_PATH = System.getProperty("user.dir") +
            "/src/test/resources/testdata/LoginData.xlsx";
     */

    private static final String LOGIN_EXCEL_FILE_PATH = "src/test/resources/testdata/LoginData.xlsx";

    private static final String LOGIN_SHEET_NAME = "LoginCredentials";

    /**
      @return A 2D Object array containing username, password, and expected result.
     */
    @DataProvider(name = "loginData")
    public Object[][] getLoginData() throws IOException {
        System.out.println("Loading login data from Excel via external DataProvider: " + LOGIN_EXCEL_FILE_PATH + " - Sheet: " + LOGIN_SHEET_NAME);

        System.out.println("DEBUG: TestDataProviders.getLoginData() is being called.");
        System.out.println("DEBUG: Attempting to read from file: " + LOGIN_EXCEL_FILE_PATH + " - Sheet: " + LOGIN_SHEET_NAME);

        Object[][] data = ExcelUtils.getTableArray(LOGIN_EXCEL_FILE_PATH, LOGIN_SHEET_NAME);

        System.out.println("DEBUG: DataProvider returned " + (data != null ? data.length : 0) + " rows.");

        return data;
    }

    // You can add more @DataProvider methods here for other test data needs
    // e.g., for product data, user registration data, etc.
    // @DataProvider(name = "productData")
    // public Object[][] getProductData() throws IOException {
    //    return ExcelUtils.getTableArray("path/to/productdata.xlsx", "Products");
    // }
}