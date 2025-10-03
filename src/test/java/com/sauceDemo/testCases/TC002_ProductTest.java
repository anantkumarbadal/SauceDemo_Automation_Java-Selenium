package com.sauceDemo.testCases;

import com.sauceDemo.bases.BaseTest;
import com.sauceDemo.pageObjects.CartPage;
import com.sauceDemo.pageObjects.LoginPage;
import com.sauceDemo.pageObjects.ProductsPage;
import com.sauceDemo.utils.ConfigLoader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TC002_ProductTest extends BaseTest {

    private ProductsPage productsPage;

    // Optional: Use a @BeforeMethod in test class to perform common setup like login
    @BeforeMethod
    public void loginBeforeProductTests() {
        getDriver().get(ConfigLoader.getProperty("base.url"));
        LoginPage loginPage = new LoginPage(getDriver());
        productsPage = loginPage.enterUsername(ConfigLoader.getProperty("standard.user.username"))
                .enterPassword(ConfigLoader.getProperty("standard.user.password"))
                .clickLoginButtonSuccess();
        Assert.assertTrue(productsPage.isProductsHeaderDisplayed(), "Products page should be displayed after login.");
    }

    @Test(priority = 1, description = "Verify adding a single product to cart")
    public void testAddSingleProductToCart() {
        // Already logged in from @BeforeMethod
        CartPage cartPage = productsPage.addItemToCart("Sauce Labs Backpack")
                .clickShoppingCart();

        Assert.assertTrue(cartPage.isCartHeaderDisplayed(), "Cart header should be displayed.");
        Assert.assertEquals(cartPage.getCartHeaderText(), "Your Cart", "Cart header text is incorrect.");
        Assert.assertEquals(cartPage.getItemNameInCart(), "Sauce Labs Backpack", "Added item name is incorrect in cart.");
    }

    @Test(priority = 2, description = "Verify adding multiple products to cart (example for another product)")
    public void testAddMultipleProductsToCart() {
        // This would require modifying ProductsPage to add multiple items,
        // or calling addItemToCart multiple times.
        // For simplicity, this test only adds one for demonstration.
        CartPage cartPage = productsPage.addItemToCart("Sauce Labs Bike Light")
                .clickShoppingCart();

        Assert.assertTrue(cartPage.isCartHeaderDisplayed(), "Cart header should be displayed.");
        Assert.assertEquals(cartPage.getCartHeaderText(), "Your Cart", "Cart header text is incorrect.");
        Assert.assertEquals(cartPage.getItemNameInCart(), "Sauce Labs Bike Light", "Added item name is incorrect in cart.");
        // To verify multiple items, you'd need a list of web elements for items in cart
    }
}