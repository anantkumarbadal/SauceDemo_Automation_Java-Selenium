package com.sauceDemo.testCases;

import com.sauceDemo.bases.BaseTest;
import com.sauceDemo.testngUtils.TestListener;
import com.sauceDemo.pageObjects.LoginPage;
import com.sauceDemo.pageObjects.ProductsPage;
import com.sauceDemo.utils.ConfigLoader;
import com.sauceDemo.utils.WebDriverUtils;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TC012_WebDriverUtilsDemoTest extends BaseTest {

    @Test(description = "Demonstrate usage of custom WebDriver utility methods.")
    public void demonstrateWebDriverUtils() {
        LoginPage loginPage = new LoginPage(getDriver());
        ProductsPage productsPage = new ProductsPage(getDriver());

        String baseUrl = ConfigLoader.getProperty("base.url");
        String standardUsername = ConfigLoader.getProperty("standard.user.username");
        String standardPassword = ConfigLoader.getProperty("standard.user.password");

        TestListener.getExtentTest().info("Navigating to base URL: " + baseUrl);
        getDriver().get(baseUrl);

        TestListener.getExtentTest().info("Attempting login with user: <b>" + standardUsername + "</b>.");
        loginPage.enterUsername(standardUsername)
                .enterPassword(standardPassword)
                .clickLoginButtonSuccess();
        TestListener.getExtentTest().pass("Successfully logged in.");

        // --- Demonstration 1: JavaScript Execution (Scroll) ---
        TestListener.getExtentTest().info("Demonstrating JavaScript execution: Scrolling to bottom.");
        WebDriverUtils.scrollToBottom(getDriver());
        TestListener.getExtentTest().pass("Scrolled to bottom of the page.");

        try {
            Thread.sleep(1000); // For visual observation
        } catch (InterruptedException e) { /* ignored */ }

        TestListener.getExtentTest().info("Demonstrating JavaScript execution: Scrolling to top.");
        WebDriverUtils.scrollToTop(getDriver());
        TestListener.getExtentTest().pass("Scrolled to top of the page.");

        try {
            Thread.sleep(1000); // For visual observation
        } catch (InterruptedException e) { /* ignored */ }

        // --- Demonstration 2: Frame Handling (if applicable, SauceDemo has no iframes, so this will be theoretical) ---
        TestListener.getExtentTest().info("Demonstrating (theoretical) Frame Handling using WebDriverUtils.");
        // Example: If SauceDemo had an iframe with id "myFrame", you'd do:
        try {
            WebDriverUtils.switchToFrameByNameOrId(getDriver(), "myFrame");
            TestListener.getExtentTest().info("Switched to frame 'myFrame'.");
            // Perform actions within the frame
            WebDriverUtils.switchToDefaultContent(getDriver());
            TestListener.getExtentTest().pass("Switched back to default content.");
        } catch (Exception e) {
            TestListener.getExtentTest().warning("Could not demonstrate frame switching (no iframe found): " + e.getMessage());
        }
        TestListener.getExtentTest().info("Skipping actual frame demonstration as SauceDemo has no iframes.");
        TestListener.getExtentTest().pass("Frame handling utility methods exist for future use.");


        // --- Demonstration 3:  ---

        TestListener.getExtentTest().pass("WebDriver utility methods demonstration complete.");
    }
}
