package com.sauceDemo.testsPractice;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;



public class StandaloneLoginTest {

    private WebDriver driver;

    @BeforeMethod
    public void setup() {
        System.out.println("DEBUG: @BeforeMethod - Setting up WebDriver...");

        driver = new ChromeDriver();
        driver.manage().window().maximize();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        System.out.println("DEBUG: WebDriver setup complete.");
    }

    @AfterMethod
    public void teardown() {
        System.out.println("DEBUG: @AfterMethod - Quitting WebDriver...");
        if (driver != null) {
            driver.quit();
        }
        System.out.println("DEBUG: WebDriver quit.");
    }

    @DataProvider(name = "loginData")
    public Object[][] loginDataProvider() {
        System.out.println("DEBUG: @DataProvider - loginDataProvider() is being called.");
        String excelFilePath = "src/test/resources/testdata/LoginData.xlsx";
        String sheetName = "LoginCredentials";
        FileInputStream excelFile = null;
        Workbook workbook = null;
        Object[][] data = null;

        try {
            File file = new File(excelFilePath);
            if (!file.exists()) {
                System.err.println("ERROR: Excel file not found at: " + excelFilePath);
                return new Object[][]{}; // Return empty array if file not found
            }
            System.out.println("DEBUG: Excel file found at: " + excelFilePath);

            excelFile = new FileInputStream(file);
            System.out.println("DEBUG: FileInputStream created for: " + excelFilePath);

            // --- CRITICAL LINE FOR POI INSTANTIATION ---
            workbook = new XSSFWorkbook(excelFile); // This is where the silent failure is expected
            System.out.println("DEBUG: XSSFWorkbook successfully initialized for: " + excelFilePath);

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                System.err.println("ERROR: Sheet '" + sheetName + "' not found in Excel file: " + excelFilePath);
                return new Object[][]{};
            }
            System.out.println("DEBUG: Sheet '" + sheetName + "' found.");

            int lastRowNum = sheet.getLastRowNum(); // Get last row number (0-based)
            int lastCellNum = sheet.getRow(0) != null ? sheet.getRow(0).getLastCellNum() : 0; // Get last cell number from header row

            System.out.println("DEBUG: Total rows (data): " + lastRowNum); // Excluding header
            System.out.println("DEBUG: Total columns: " + lastCellNum);

            // Assuming first row is header, so start from 1
            data = new Object[lastRowNum][lastCellNum];

            for (int i = 1; i <= lastRowNum; i++) { // Loop through rows, skipping header (i=1)
                Row row = sheet.getRow(i);
                if (row == null) {
                    System.out.println("WARNING: Skipping null row at index " + i);
                    continue;
                }
                for (int j = 0; j < lastCellNum; j++) { // Loop through cells
                    Cell cell = row.getCell(j);
                    if (cell != null) {
                        data[i - 1][j] = cell.toString(); // Store cell value
                    } else {
                        data[i - 1][j] = ""; // Treat null cells as empty string
                    }
                    System.out.println("DEBUG: Read cell [" + (i - 1) + "][" + j + "]: " + data[i - 1][j]);
                }
            }
            System.out.println("DEBUG: Data successfully read from Excel.");

        } catch (IOException e) {
            System.err.println("ERROR (IOException): Failed to read Excel file or initialize workbook.");
            System.err.println("Reason: " + e.getMessage());
            e.printStackTrace();
            return new Object[][]{};
        } catch (Exception e) { // Catch any other exceptions
            System.err.println("ERROR (General Exception): An unexpected error occurred during Excel processing.");
            System.err.println("Reason: " + e.getMessage());
            e.printStackTrace();
            return new Object[][]{};
        } catch (Error e) { // IMPORTANT: Catch Error for deeper JVM/Classpath issues
            System.err.println("FATAL ERROR: An ERROR occurred during Excel processing. This indicates a severe JVM or classpath issue.");
            System.err.println("Reason: " + e.getMessage());
            e.printStackTrace();
            return new Object[][]{};
        } finally {
            try {
                if (workbook != null) workbook.close();
                if (excelFile != null) excelFile.close();
                System.out.println("DEBUG: Excel file streams closed.");
            } catch (IOException ex) {
                System.err.println("ERROR: Failed to close Excel file streams: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        return data;
    }

    @Test(dataProvider = "loginData")
    public void testLoginWithExcelData(String username, String password, String expectedResult) {
        System.out.println("\nDEBUG: @Test - Running testLoginWithExcelData for User: " + username + ", Pass: " + password + ", Expected: " + expectedResult);

        // Navigate to the login page
        driver.get("https://www.saucedemo.com/");
        System.out.println("DEBUG: Navigated to Sauce Demo login page.");

        // Find elements and perform login
        WebElement usernameField = driver.findElement(By.id("user-name"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login-button"));

        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginButton.click();
        System.out.println("DEBUG: Entered credentials and clicked login.");

        // Dynamic assertion based on expectedResult
        if (expectedResult.equalsIgnoreCase("Success")) {
            try {
                WebElement productsHeader = driver.findElement(By.className("title"));
                Assert.assertEquals(productsHeader.getText(), "Products", "Login successful, but 'Products' header not found or incorrect.");
                System.out.println("Test PASSED: Successfully logged in with " + username);
            } catch (org.openqa.selenium.NoSuchElementException e) {
                System.err.println("Test FAILED: Login expected to be successful, but 'Products' header not found for " + username);
                e.printStackTrace();
                Assert.fail("Login expected to be successful, but Products page not reached.");
            }
        } else { // Assuming any other 'expectedResult' means a failed login with an error message
            try {
                WebElement errorMessage = driver.findElement(By.cssSelector("h3[data-test='error']"));
                String actualErrorMessage = errorMessage.getText();
                System.out.println("DEBUG: Found error message: " + actualErrorMessage);
                // Assert that the error message contains the expected part (case-insensitive for robustness)
                Assert.assertTrue(actualErrorMessage.toLowerCase().contains(expectedResult.toLowerCase()),
                        "Login failed, but error message does not contain expected text. Expected: '" + expectedResult + "', Actual: '" + actualErrorMessage + "'");
                System.out.println("Test PASSED: Login failed as expected for " + username + " with error message containing '" + expectedResult + "'");
            } catch (org.openqa.selenium.NoSuchElementException e) {
                System.err.println("Test FAILED: Login expected to fail with error '" + expectedResult + "', but no error message element found for " + username);
                e.printStackTrace();
                Assert.fail("Login expected to fail, but no error message element found.");
            }
        }
    }


}
