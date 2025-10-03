package com.sauceDemo.testCases;

import com.sauceDemo.bases.BaseTest;
import com.sauceDemo.pageObjects.*;
import com.sauceDemo.utils.ConfigLoader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TC003_CheckoutTest extends BaseTest {

    private ProductsPage productsPage;

    @BeforeMethod
    public void loginAndAddProduct() {
        getDriver().get(ConfigLoader.getProperty("base.url"));
        LoginPage loginPage = new LoginPage(getDriver());
        productsPage = loginPage.enterUsername(ConfigLoader.getProperty("standard.user.username"))
                .enterPassword(ConfigLoader.getProperty("standard.user.password"))
                .clickLoginButtonSuccess();
        Assert.assertTrue(productsPage.isProductsHeaderDisplayed(), "Products page should be displayed after login.");

        productsPage.addItemToCart("Sauce Labs Backpack"); // Add a default item for checkout
    }

    @Test(priority = 1, description = "Verify successful product checkout flow")
    public void testSuccessfulProductCheckout() {
        // Already logged in and product added from @BeforeMethod
        CartPage cartPage = productsPage.clickShoppingCart();

        Assert.assertTrue(cartPage.isCartHeaderDisplayed(), "Cart header should be displayed.");

        CheckoutInformationPage checkoutOnePage = cartPage.clickCheckout();

        Assert.assertTrue(checkoutOnePage.isCheckoutHeaderDisplayed(), "Checkout Step One header should be displayed.");
        Assert.assertEquals(checkoutOnePage.getCheckoutHeaderText(), "Checkout: Your Information", "Checkout header text is incorrect.");

        CheckoutOverviewPage checkoutTwoPage = checkoutOnePage.enterYourInformation("Jane", "Doe", "90210")
                .clickContinue();

        Assert.assertTrue(checkoutTwoPage.isCheckoutOverviewHeaderDisplayed(), "Checkout Step Two header should be displayed.");
        Assert.assertEquals(checkoutTwoPage.getCheckoutOverviewHeaderText(), "Checkout: Overview", "Checkout overview header text is incorrect.");
        Assert.assertTrue(checkoutTwoPage.getItemTotal().contains("$29.99"), "Item total should be displayed correctly.");

        CheckoutCompletePage checkoutCompletePage = checkoutTwoPage.clickFinish();

        Assert.assertTrue(checkoutCompletePage.isCompleteHeaderDisplayed(), "Checkout Complete header should be displayed.");
        Assert.assertEquals(checkoutCompletePage.getCompleteHeaderText(), "Checkout: Complete!", "Checkout complete header text is incorrect.");
        Assert.assertEquals(checkoutCompletePage.getCompleteDispatchMessageText(), "Your order has been dispatched, and will arrive just as fast as the pony can get there!", "Complete text is incorrect.");

        // Optionally, navigate back to products page
        productsPage = checkoutCompletePage.clickBackToHome();
        Assert.assertTrue(productsPage.isProductsHeaderDisplayed(), "Should be back on Products page.");
    }

    @Test(priority = 2, description = "Verify cancelling checkout from information page")
    public void testCancelCheckoutFromInformationPage() {
        // Already logged in and product added from @BeforeMethod
        CartPage cartPage = productsPage.clickShoppingCart();
        CheckoutInformationPage checkoutOnePage = cartPage.clickCheckout();

        // CORRECTED LINE: Capture the returned CartPage object
        CartPage cartPageAfterCancel = checkoutOnePage.clickCancel();

        // ASSERTION: Verify that we are indeed back on the Cart Page
        Assert.assertTrue(cartPageAfterCancel.isCartHeaderDisplayed(), "Should be back on Cart page after cancelling checkout from Information page.");
        Assert.assertEquals(cartPageAfterCancel.getCartHeaderText(), "Your Cart", "Cart header text is incorrect after cancel.");
    }
}