package com.sauceDemo.testCases;

import com.sauceDemo.bases.BaseTest;
import com.sauceDemo.pageObjects.*;
import com.sauceDemo.testngUtils.TestListener;
import com.sauceDemo.utils.ConfigLoader;
import com.sauceDemo.utils.FakerUtils;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TC009_DynamicCheckoutInfoTest extends BaseTest {

    @Test(description = "Verify end-to-end purchase flow using dynamically generated checkout information.")
    public void verifyPurchaseFlowWithFakerData() {

        LoginPage loginPage = new LoginPage(getDriver());
        ProductsPage productsPage = new ProductsPage(getDriver());
        CartPage cartPage = new CartPage(getDriver());
        CheckoutInformationPage checkoutInformationPage = new CheckoutInformationPage(getDriver());
        CheckoutOverviewPage checkoutOverviewPage = new CheckoutOverviewPage(getDriver());
        CheckoutCompletePage checkoutCompletePage = new CheckoutCompletePage(getDriver());
        DashboardPage dashboardPage = new DashboardPage(getDriver());

        String baseUrl = ConfigLoader.getProperty("base.url");
        String standardUsername = ConfigLoader.getProperty("standard.user.username");
        String standardPassword = ConfigLoader.getProperty("standard.user.password");

        // --- Step 2: Generate Dynamic Checkout Data using FakerUtils ---
        String dynamicFirstName = FakerUtils.generateFirstName();
        String dynamicLastName = FakerUtils.generateLastName();
        String dynamicZipCode = FakerUtils.generateZipCode();

        TestListener.getExtentTest().info("<b>Generated Dynamic Checkout Info:</b><br>" +
                "First Name: " + dynamicFirstName + "<br>" +
                "Last Name: " + dynamicLastName + "<br>" +
                "Zip Code: " + dynamicZipCode);


        // --- Step 3: Pre-condition - Login ---
        TestListener.getExtentTest().info("Navigating to base URL: " + baseUrl);
        getDriver().get(baseUrl);

        TestListener.getExtentTest().info("Attempting login with user: <b>" + standardUsername + "</b>.");
        loginPage.enterUsername(standardUsername)
                .enterPassword(standardPassword)
                .clickLoginButtonSuccess();
        TestListener.getExtentTest().pass("Successfully logged in.");

        // --- Step 4: Add a Product to Cart (e.g., Sauce Labs Backpack) ---
        TestListener.getExtentTest().info("Adding 'Sauce Labs Backpack' to cart.");
        productsPage.addItemToCart("Sauce Labs Backpack");
        TestListener.getExtentTest().pass("Product added to cart.");

        // --- Step 5: Verify Cart Badge Count ---
        Assert.assertEquals(productsPage.getShoppingCartBadgeCount(), 1, "Cart badge count should be 1.");
        TestListener.getExtentTest().pass("Cart badge count verified.");

        // --- Step 6: Navigate to Cart ---
        TestListener.getExtentTest().info("Navigating to cart page.");
        productsPage.clickShoppingCart();
        TestListener.getExtentTest().pass("Navigated to cart page.");

        // --- Step 7: Proceed to Checkout Information ---
        TestListener.getExtentTest().info("Clicking 'Checkout' button.");
        cartPage.clickCheckout();
        TestListener.getExtentTest().pass("Navigated to Checkout Information page.");

        // --- Step 8: Fill Checkout Information using Faker Data ---
        TestListener.getExtentTest().info("Filling checkout information with dynamically generated data.");
        checkoutInformationPage.enterYourInformation(
                dynamicFirstName,
                dynamicLastName,
                dynamicZipCode
        );
        TestListener.getExtentTest().pass("Checkout information filled using Faker data.");

        TestListener.getExtentTest().info("Clicking 'Continue' to Checkout Overview.");
        checkoutInformationPage.clickContinue();
        TestListener.getExtentTest().pass("Navigated to Checkout Overview page.");

        // --- Step 9: Verify Checkout Overview Page Details
        TestListener.getExtentTest().info("Verifying Checkout Overview page details.");

        Assert.assertTrue(checkoutOverviewPage.getItemTotal().contains("$"), "Item Total should contain currency symbol.");
        TestListener.getExtentTest().pass("Basic Checkout Overview details verified.");

        // --- Step 10: Finish Purchase ---
        TestListener.getExtentTest().info("Clicking 'Finish' button.");
        checkoutOverviewPage.clickFinish();
        TestListener.getExtentTest().pass("Purchase completed.");

        // --- Step 11: Verify Order Confirmation ---
        TestListener.getExtentTest().info("Verifying order completion message.");
        Assert.assertTrue(checkoutCompletePage.isOrderCompleteMessageDisplayed(), "Order complete message not displayed.");

        String actualConfirmationMessage = checkoutCompletePage.getConfirmationMessageText();
        Assert.assertEquals(actualConfirmationMessage, ConfigLoader.getProperty("order.thankyou.message"), "Order confirmation message mismatch.");

        TestListener.getExtentTest().pass("<b>Order Confirmed!</b> Message: '" + actualConfirmationMessage + "'");

        // ---Step 12: Verify Order dispatch message
        TestListener.getExtentTest().info("Verifying order dispatch message.");
        Assert.assertTrue(checkoutCompletePage.isCompleteDispatchMessageDisplayed(), "Order dispatch message not displayed.");

        String actualDispatchMessage = checkoutCompletePage.getCompleteDispatchMessageText();
        Assert.assertEquals(actualDispatchMessage, ConfigLoader.getProperty("order.dispatch.message"), "Order dispatch message mismatch.");

        TestListener.getExtentTest().pass("<b>Order Dispatched!<b> Message: '" + actualDispatchMessage + "'");

        // ---Step 13: Verify go back to Dashboard Page
        TestListener.getExtentTest().info("Verifying the Back Home Button");
        Assert.assertTrue(checkoutCompletePage.isBackHomeButtonEnabled(), "Back Home button is not interactable");

        checkoutCompletePage.clickBackToHome();
        TestListener.getExtentTest().pass("Back Home button clicked");
        Assert.assertTrue(productsPage.isProductsHeaderDisplayed(), "Should be back on Products page.");

        // ---Step 14: Verify Logout
        TestListener.getExtentTest().info("Verifying the Hamburger Menu- Logout");
        Assert.assertTrue(dashboardPage.isHamburgerMenuButtonDisplayed(), "Hamburger Menu button is not displayed");

        dashboardPage.openHamburgerMenu();
        TestListener.getExtentTest().pass("Hamburger Menu Opened");

        Assert.assertTrue(dashboardPage.isLogoutButtonDisplayed(), "Logout button is not displayed");
        dashboardPage.clickLogout();
        TestListener.getExtentTest().pass("Successfully logged out.");

        Assert.assertEquals(getDriver().getCurrentUrl(), baseUrl, "");
        TestListener.getExtentTest().info("Index Page displayed after Logout");


    }


}
