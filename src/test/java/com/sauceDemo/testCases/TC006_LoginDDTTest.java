package com.sauceDemo.testCases;

import com.sauceDemo.bases.BaseTest;
import com.sauceDemo.dataProviders.TestDataProviders; // Import the new DataProvider class
import com.sauceDemo.pageObjects.LoginPage;
import com.sauceDemo.pageObjects.ProductsPage;
import com.sauceDemo.utils.ConfigLoader;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC006_LoginDDTTest extends BaseTest {

    /**
     * Test method to perform login using data from the DataProvider.
     * This test will run multiple times, once for each row in the Excel sheet.
     */

    @Test(dataProvider = "loginData", dataProviderClass = TestDataProviders.class,
            description = "Data-driven test for various login scenarios")
    public void testLoginDataDrivenTest(String username, String password, String expectedResult) {
        System.out.println("\n--- Running Login DDT Test for: User=" + username + ", Expected=" + expectedResult + " ---");

        getDriver().get(ConfigLoader.getProperty("base.url"));

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.enterUsername(username).enterPassword(password);

        // Decide assertion based on expectedResult
        if (expectedResult.equalsIgnoreCase("Success")) {
            ProductsPage productsPage = loginPage.clickLoginButtonSuccess();
            Assert.assertTrue(productsPage.isProductsHeaderDisplayed(),
                    "Login for " + username + " was expected to be successful, but Products header not displayed.");
            Assert.assertEquals(productsPage.getProductsHeaderText(), "Products",
                    "Products header text for " + username + " is incorrect after successful login.");
            System.out.println("Login for user " + username + " successful as expected.");
        } else if (expectedResult.equalsIgnoreCase("Failure")) {
            loginPage.clickLoginButtonFailure(); // This clicks the button and expects a failure
            Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                    "Login for " + username + " was expected to fail, but error message not displayed.");
            System.out.println("Login for user " + username + " failed as expected. Error: " + loginPage.getErrorMessageText());
        } else {
            Assert.fail("Invalid 'ExpectedResult' in Excel for user: " + username + ". Value: " + expectedResult);
        }
    }
}