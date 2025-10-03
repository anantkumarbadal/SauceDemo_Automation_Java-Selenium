package com.sauceDemo.testCases;

import com.sauceDemo.bases.BaseTest;
import com.sauceDemo.pageObjects.LoginPage;
import com.sauceDemo.pageObjects.ProductsPage;
import com.sauceDemo.utils.ConfigLoader;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC001_LoginTest extends BaseTest {

    @Test(priority = 1, description = "Verify successful login with valid credentials")
    public void testSuccessfulLoginUser() {
        getDriver().get(ConfigLoader.getProperty("base.url"));

        LoginPage loginPage = new LoginPage(getDriver());

        ProductsPage productsPage = loginPage.enterUsername(ConfigLoader.getProperty("standard.user.username"))
                .enterPassword(ConfigLoader.getProperty("standard.user.password"))
                .clickLoginButtonSuccess();

        Assert.assertTrue(productsPage.isProductsHeaderDisplayed(), "Products header should be displayed after successful login.");
        Assert.assertEquals(productsPage.getProductsHeaderText(), "Products", "Products header text should be 'Products'.");
        System.out.println("Login Successful and Products Page Verified.");
    }

    @Test(priority = 2, description = "Verify login with problem user credentials")
    public void testLoginProblemUser() {
        getDriver().get(ConfigLoader.getProperty("base.url"));

        LoginPage loginPage = new LoginPage(getDriver());

        ProductsPage productsPage = loginPage.enterUsername(ConfigLoader.getProperty("problem.user.username"))
                .enterPassword(ConfigLoader.getProperty("problem.user.password"))
                .clickLoginButtonSuccess();

        Assert.assertTrue(productsPage.isProductsHeaderDisplayed(), "Products header should be displayed after problem user login.");
        Assert.assertEquals(productsPage.getProductsHeaderText(), "Products", "Products header text should be 'Products'.");
        // You might add specific assertions here for the 'problem user' behavior if applicable
    }

    @Test(priority = 3, description = "Verify login with performance glitch user credentials")
    public void testLoginPerformanceGlitchUser() {
        getDriver().get(ConfigLoader.getProperty("base.url"));

        LoginPage loginPage = new LoginPage(getDriver());

        ProductsPage productsPage = loginPage.enterUsername(ConfigLoader.getProperty("performance.glitch.user.username"))
                .enterPassword(ConfigLoader.getProperty("performance.glitch.user.password"))
                .clickLoginButtonSuccess();

        Assert.assertTrue(productsPage.isProductsHeaderDisplayed(), "Products header should be displayed after performance glitch user login.");
        Assert.assertEquals(productsPage.getProductsHeaderText(), "Products", "Products header text should be 'Products'.");
        // You might add assertions related to performance if you're measuring it
    }
}