package com.sauceDemo.testCases;

import com.sauceDemo.bases.BaseTest;
import com.sauceDemo.testngUtils.TestListener;
import com.sauceDemo.pageObjects.LoginPage;
import com.sauceDemo.pageObjects.ProductsPage;
import com.sauceDemo.pageObjects.CartPage;
import com.sauceDemo.pageObjects.CheckoutInformationPage;
import com.sauceDemo.pageObjects.CheckoutOverviewPage;
import com.sauceDemo.pageObjects.CheckoutCompletePage;
import com.sauceDemo.utils.ConfigLoader;
import com.sauceDemo.pojoClasses.CheckoutDetails;
import com.sauceDemo.dataBuilders.CheckoutDetailsBuilder;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TC010_BuilderPatternCheckoutTest extends BaseTest {

    @Test(description = "Verify end-to-end purchase flow using Test Data Builder for CheckoutInfo.")
    public void tc010_verifyPurchaseFlowWithBuilderData() {
        // --- Step 1: Initialize Page Objects and get configuration ---
        LoginPage loginPage = new LoginPage(getDriver());
        ProductsPage productsPage = new ProductsPage(getDriver());
        CartPage cartPage = new CartPage(getDriver());
        CheckoutInformationPage checkoutInformationPage = new CheckoutInformationPage(getDriver());
        CheckoutOverviewPage checkoutOverviewPage = new CheckoutOverviewPage(getDriver());
        CheckoutCompletePage checkoutCompletePage = new CheckoutCompletePage(getDriver());

        String baseUrl = ConfigLoader.getProperty("base.url");
        String standardUsername = ConfigLoader.getProperty("standard.user.username");
        String standardPassword = ConfigLoader.getProperty("standard.user.password");

        // --- Step 2: Create CheckoutDetails using CheckoutDetailsBuilder ---
        // This is where we leverage the builder pattern!
        // Example 1: Build with all default Faker data
        CheckoutDetails defaultCheckoutDetails = new CheckoutDetailsBuilder().build();

        // Example 2: Build with specific first name, Faker for others
        CheckoutDetails customFirstNameDetails = new CheckoutDetailsBuilder()
                .withFirstName("BuilderTest")
                .build();

        // Example 3: Build a specific error scenario (e.g., empty zip code)
        CheckoutDetails emptyZipCodeDetails = CheckoutDetailsBuilder.createWithEmptyZipCode();

        // For this test, let's use the default fully faked data
        CheckoutDetails checkoutInfo = defaultCheckoutDetails; // Or customFirstNameDetails, or emptyZipCodeDetails

        TestListener.getExtentTest().info("<b>Using Checkout Info (Builder Pattern):</b><br>" +
                "First Name: " + checkoutInfo.getFirstName() + "<br>" +
                "Last Name: " + checkoutInfo.getLastName() + "<br>" +
                "Zip Code: " + checkoutInfo.getZipCode());


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

        // --- Step 8: Fill Checkout Information using the Built Data ---
        TestListener.getExtentTest().info("Filling checkout information using data built by CheckoutDetailsBuilder.");
        checkoutInformationPage.enterYourInformation(
                checkoutInfo.getFirstName(),
                checkoutInfo.getLastName(),
                checkoutInfo.getZipCode()
        );
        TestListener.getExtentTest().pass("Checkout information filled successfully.");

        TestListener.getExtentTest().info("Clicking 'Continue' to Checkout Overview.");
        checkoutInformationPage.clickContinue();

        // --- Step 9: Handle Potential Error or Success Flow ---
        // We'll run a success flow for this example. If you choose emptyZipCodeDetails,
        // you'd add an assertion for the error message here.
        if (checkoutInformationPage.isErrorMessageDisplayed()) {
            TestListener.getExtentTest().fail("Unexpected error message displayed: " + checkoutInformationPage.getErrorMessageText());
            Assert.fail("Test failed due to unexpected error on checkout information page.");
        }
        TestListener.getExtentTest().pass("Navigated to Checkout Overview page.");

        // --- Step 10: Verify Checkout Overview Page Details (simplified) ---
        TestListener.getExtentTest().info("Verifying Checkout Overview page details.");
        Assert.assertTrue(checkoutOverviewPage.getItemTotal().contains("$"), "Item Total should contain currency symbol.");
        TestListener.getExtentTest().pass("Basic Checkout Overview details verified.");

        // --- Step 11: Finish Purchase ---
        TestListener.getExtentTest().info("Clicking 'Finish' button.");
        checkoutOverviewPage.clickFinish();
        TestListener.getExtentTest().pass("Purchase completed.");

        // --- Step 12: Verify Order Confirmation ---
        TestListener.getExtentTest().info("Verifying order completion message.");
        Assert.assertTrue(checkoutCompletePage.isOrderCompleteMessageDisplayed(), "Order complete message not displayed.");
        String actualConfirmationMessage = checkoutCompletePage.getConfirmationMessageText();
        Assert.assertEquals(actualConfirmationMessage, ConfigLoader.getProperty("order.thankyou.message"), "Order confirmation message mismatch.");
        TestListener.getExtentTest().pass("<b>Order Confirmed!</b> Message: '" + actualConfirmationMessage + "'");
    }


}

/*
When Test Data Builders Become Highly Beneficial/Necessary:
Test Data Builders shine in scenarios where you need to programmatically
create test data objects (not load them from files) that are:

Complex: Have many fields, nested objects, or optional fields.

Varied: You need to create many different variations of an object for
various test cases (e.g., a "valid user," an "invalid user with missing email," a "user with admin role," a "user with specific purchase history").

Readable and Maintainable: You want the data creation in your test methods
to be self-documenting and easy to understand at a glance.

Reusable: You want to define common "default" states for objects and then
easily override specific fields for a given test.

Decoupling: You want to protect your test methods from changes in the POJO's
constructor or field count.

For example, if you had a User POJO with 15 fields (name, address, contact,
roles, status, preferences, etc.), creating instances directly would lead to very long, unreadable constructors or repetitive setter calls. A builder would make it like new UserBuilder().withName("Alice").asAdmin().withStatus("Active").build().
 */
