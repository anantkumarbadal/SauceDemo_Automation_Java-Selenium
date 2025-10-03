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
import com.sauceDemo.utils.JsonDataReader; // Import our JSON data reader
import com.sauceDemo.pojoClasses.PurchaseScenario; // Import our PurchaseScenario POJO

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List; // For List<String>


public class TC008_PurchaseFlowJson extends BaseTest {

    // --- Data Provider to read purchase scenarios from JSON ---
    @DataProvider(name = "purchaseScenarios")
    public Object[] getPurchaseScenariosData() {
        // Use our JsonDataReader to get the list of scenarios
        List<PurchaseScenario> scenarios = JsonDataReader.getPurchaseScenarios("purchase_scenarios.json");

        // Convert the List to an array of Objects for TestNG DataProvider
     //   return scenarios != null ? scenarios.toArray() : new Object[0];

        // Convert the List<PurchaseScenario> into Object[][] required by DataProvider
        // Each inner array will contain [String testTitle, PurchaseScenario scenarioObject]
        Object[][] data = new Object[scenarios.size()][2]; // 2 columns: 1 for title, 1 for scenario object

        for (int i = 0; i < scenarios.size(); i++) {
            PurchaseScenario scenario = scenarios.get(i);
            // Column 0: Short title for the report (TestListener will pick this as "username")
            data[i][0] = scenario.getScenarioName();
            // Column 1: The full PurchaseScenario object
            data[i][1] = scenario;
        }
        return data;


    }

    // --- Test Method driven by Data Provider ---
    @Test(dataProvider = "purchaseScenarios",
            description = "Verify end-to-end purchase flow with dynamic product and checkout data.")
    public void verifyPurchaseFlowWithDynamicData(String scenarioNameForReport, PurchaseScenario scenario) {

        // --- Step 1: Log Test Scenario Details ---
        TestListener.getExtentTest().info("<b>Starting Purchase Scenario: " + scenario.getScenarioName() + "</b>");
        TestListener.getExtentTest().info("Products to add: " + scenario.getProductsToAdd());
        TestListener.getExtentTest().info("Checkout Details: First Name=" + scenario.getCheckoutDetails().getFirstName() +
                ", Last Name=" + scenario.getCheckoutDetails().getLastName() +
                ", Zip Code=" + scenario.getCheckoutDetails().getZipCode());

        // --- Step 2: Initialize Page Objects and get configuration ---
        LoginPage loginPage = new LoginPage(getDriver());
        ProductsPage productsPage = new ProductsPage(getDriver());
        CartPage cartPage = new CartPage(getDriver());
        CheckoutInformationPage checkoutInformationPage = new CheckoutInformationPage(getDriver());
        CheckoutOverviewPage checkoutOverviewPage = new CheckoutOverviewPage(getDriver());
        CheckoutCompletePage checkoutCompletePage = new CheckoutCompletePage(getDriver());

        String baseUrl = ConfigLoader.getProperty("base.url");
        String standardUsername = ConfigLoader.getProperty("standard.user.username");
        String standardPassword = ConfigLoader.getProperty("standard.user.password");

        // --- Step 3: Pre-condition - Login ---
        TestListener.getExtentTest().info("Navigating to base URL: " + baseUrl);
        getDriver().get(baseUrl);

        TestListener.getExtentTest().info("Attempting login with user: <b>" + standardUsername + "</b>.");
        loginPage.enterUsername(standardUsername)
                .enterPassword(standardPassword)
                .clickLoginButtonSuccess();
        TestListener.getExtentTest().pass("Successfully logged in.");

        // --- Step 4: Add Products to Cart ---
        TestListener.getExtentTest().info("Adding products to cart...");
        if (scenario.getProductsToAdd() != null && !scenario.getProductsToAdd().isEmpty()) {
            for (String product : scenario.getProductsToAdd()) {
                productsPage.addItemToCart(product); // Using the method we confirmed/added
                TestListener.getExtentTest().info("Added '" + product + "' to cart.");
            }
        } else {
            TestListener.getExtentTest().warning("No products specified to add for this scenario.");
        }

        // --- Step 5: Verify Cart Badge Count ---
        int expectedCartCount = scenario.getProductsToAdd() != null ? scenario.getProductsToAdd().size() : 0;
        int actualCartCount = productsPage.getShoppingCartBadgeCount(); // Using the method we added
        TestListener.getExtentTest().info("Expected cart badge count: " + expectedCartCount + ", Actual: " + actualCartCount);
        Assert.assertEquals(actualCartCount, expectedCartCount, "Cart badge count mismatch.");
        TestListener.getExtentTest().pass("Cart badge count verified.");

        // --- Step 6: Navigate to Cart ---
        TestListener.getExtentTest().info("Navigating to cart page.");
        productsPage.clickShoppingCart(); // Using the available method

        // --- Step 7: Verify Items in Cart ---
        List<String> actualCartItemNames = cartPage.getCartItemNames(); // Using the method we added
        TestListener.getExtentTest().info("Products in cart (Actual): " + actualCartItemNames);
        TestListener.getExtentTest().info("Products to add (Expected): " + scenario.getProductsToAdd());

        Assert.assertTrue(actualCartItemNames.containsAll(scenario.getProductsToAdd()) &&
                        scenario.getProductsToAdd().containsAll(actualCartItemNames),
                "Mismatch in actual vs expected cart items.");
        TestListener.getExtentTest().pass("Cart items verified successfully.");


        // --- Step 8: Proceed to Checkout Information ---
        TestListener.getExtentTest().info("Clicking 'Checkout' button.");
        cartPage.clickCheckout(); // Using the available method

        // --- Step 9: Fill Checkout Information ---
        TestListener.getExtentTest().info("Entering checkout information: " +
                scenario.getCheckoutDetails().getFirstName() + ", " +
                scenario.getCheckoutDetails().getLastName() + ", " +
                scenario.getCheckoutDetails().getZipCode());
        checkoutInformationPage.enterYourInformation(
                scenario.getCheckoutDetails().getFirstName(),
                scenario.getCheckoutDetails().getLastName(),
                scenario.getCheckoutDetails().getZipCode()
        ); // Using the available combined method

        TestListener.getExtentTest().info("Clicking 'Continue' to Checkout Overview.");
        checkoutInformationPage.clickContinue(); // Using the available method

        // --- Step 10: Handle Expected Outcomes (Error or Success Flow) ---
        if (scenario.getExpectedErrorMessage() != null) {
            // This is an error scenario (e.g., empty zip code, empty cart checkout attempt)
            TestListener.getExtentTest().info("Expecting an error message.");
            Assert.assertTrue(checkoutInformationPage.isErrorMessageDisplayed(), "Error message should be displayed.");
            String actualErrorMessage = checkoutInformationPage.getErrorMessageText();
            TestListener.getExtentTest().info("Actual error message: '" + actualErrorMessage + "'");
            Assert.assertEquals(actualErrorMessage, scenario.getExpectedErrorMessage(), "Error message text mismatch.");
            TestListener.getExtentTest().pass("Error scenario handled: Expected error message '" + scenario.getExpectedErrorMessage() + "' was displayed.");
        } else {
            // This is a successful purchase flow
            TestListener.getExtentTest().info("Verifying Checkout Overview page details...");

            // Verify listed items
            List<String> actualListedItemNames = checkoutOverviewPage.getListedItemNames(); // Using the method we added
            TestListener.getExtentTest().info("Products in overview (Actual): " + actualListedItemNames);
            Assert.assertTrue(actualListedItemNames.containsAll(scenario.getProductsToAdd()) &&
                            scenario.getProductsToAdd().containsAll(actualListedItemNames),
                    "Mismatch in actual vs expected items on Checkout Overview.");
            TestListener.getExtentTest().pass("Checkout Overview items verified.");

            // Verify totals
            String actualItemTotal = checkoutOverviewPage.getItemTotal(); // Using available method
            String actualTax = checkoutOverviewPage.getTax(); // Using the method we added
            String actualGrandTotal = checkoutOverviewPage.getGrandTotal(); // Using the method we added

            TestListener.getExtentTest().info("Expected Item Total: " + scenario.getExpectedItemTotal() + ", Actual: " + actualItemTotal);
            TestListener.getExtentTest().info("Expected Tax: " + scenario.getExpectedTax() + ", Actual: " + actualTax);
            TestListener.getExtentTest().info("Expected Grand Total: " + scenario.getExpectedGrandTotal() + ", Actual: " + actualGrandTotal);

            // Note: We use Objects.equals for string comparison. For numerical, parse to double.
            Assert.assertTrue(actualItemTotal.contains(scenario.getExpectedItemTotal()), "Item Total mismatch.");
            Assert.assertTrue(actualTax.contains(scenario.getExpectedTax()), "Tax mismatch.");
            Assert.assertTrue(actualGrandTotal.contains(scenario.getExpectedGrandTotal()), "Grand Total mismatch.");
            TestListener.getExtentTest().pass("All totals on Checkout Overview verified successfully.");

            // --- Step 11: Finish Purchase ---
            TestListener.getExtentTest().info("Clicking 'Finish' button.");
            checkoutOverviewPage.clickFinish(); // Using the available method

            // --- Step 12: Verify Order Confirmation ---
            TestListener.getExtentTest().info("Verifying order completion.");
            Assert.assertTrue(checkoutCompletePage.isOrderCompleteMessageDisplayed(), "Order complete message not displayed.");
            String actualConfirmationMessage = checkoutCompletePage.getConfirmationMessageText(); // Using the method we added
            TestListener.getExtentTest().info("Actual confirmation message: '" + actualConfirmationMessage + "'");
            Assert.assertEquals(actualConfirmationMessage, scenario.getExpectedConfirmationMessage(), "Order confirmation message mismatch.");
            TestListener.getExtentTest().pass("<b>Purchase Flow Completed Successfully!</b> Confirmed message: '" + actualConfirmationMessage + "'");
        }
    }
}