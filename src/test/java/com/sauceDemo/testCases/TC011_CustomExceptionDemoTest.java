package com.sauceDemo.testCases;

import com.sauceDemo.bases.BaseTest;
import com.sauceDemo.testngUtils.TestListener;
import com.sauceDemo.pageObjects.LoginPage;
import com.sauceDemo.pageObjects.ProductsPage;
import com.sauceDemo.utils.ConfigLoader;

// Import our custom exceptions
import com.sauceDemo.exceptions.FrameworkException;
import com.sauceDemo.exceptions.ElementNotFoundException;
import com.sauceDemo.exceptions.AutomationFlowException;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC011_CustomExceptionDemoTest extends BaseTest {

    @Test(description = "Demonstrate custom exception handling for ElementNotFound and AutomationFlow issues.")
    public void demonstrateCustomExceptions() {

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


        // --- Demonstration 1: ElementNotFoundException ---
        // Simulating an element not found scenario.
        // In a real framework, your Page Object method (e.g., findElement, clickElement)
        // would catch NoSuchElementException and throw your custom ElementNotFoundException.
        TestListener.getExtentTest().info("Attempting to interact with a non-existent element to demonstrate ElementNotFoundException.");
        try {
            // Deliberately try to find an element that does not exist
            getDriver().findElement(By.id("nonExistentElementID123")).click();
            TestListener.getExtentTest().fail("Unexpectedly found a non-existent element!");
            Assert.fail("Test failed: Non-existent element was unexpectedly found.");
        } catch (NoSuchElementException e) {
            // Caught the standard Selenium exception, now wrap it in our custom exception
            TestListener.getExtentTest().warning("Caught standard NoSuchElementException. Wrapping it in ElementNotFoundException.");
            throw new ElementNotFoundException(
                    "Failed to find expected element with ID 'nonExistentElementID123' on the Products Page. " +
                            "This element is critical for the test flow.", e); // Passing 'e' as cause
        } catch (ElementNotFoundException customE) { // This catch block would handle our custom exception if it was thrown by a Page Object
            TestListener.getExtentTest().fail("Caught expected ElementNotFoundException: " + customE.getMessage());
            Assert.fail("Test failed due to Element Not Found (Custom Exception): " + customE.getMessage());
        } catch (Exception unexpectedE) {
            TestListener.getExtentTest().fail("Caught an unexpected exception: " + unexpectedE.getMessage());
            Assert.fail("Test failed due to an unexpected exception: " + unexpectedE.getMessage());
        }

        // --- Demonstration 2: AutomationFlowException ---
        // Simulating an unexpected state or flow logic error.
        // For example, expecting to be on the checkout page, but landing back on login.
        TestListener.getExtentTest().info("Simulating an unexpected automation flow scenario to demonstrate AutomationFlowException.");
        try {
            // Let's pretend we expected to be on the cart page after some action,
            // but for some reason, we are still on the products page.
            if (!productsPage.isShoppingCartDisplayed()) { // Assuming this checks for a unique element on ProductsPage
                TestListener.getExtentTest().warning("Simulating unexpected page condition.");
                // In a real scenario, this check would happen after an action
                throw new AutomationFlowException("Expected to be on the Cart Page after clicking, but remained on the Products Page.");
            }
            TestListener.getExtentTest().pass("Simulated flow passed as expected (not throwing exception)."); // This line would run if the condition was met
        } catch (AutomationFlowException afe) {
            TestListener.getExtentTest().fail("Caught expected AutomationFlowException: " + afe.getMessage());
            Assert.fail("Test failed due to Automation Flow Exception: " + afe.getMessage());
        } catch (Exception e) {
            TestListener.getExtentTest().fail("Caught an unexpected exception during flow simulation: " + e.getMessage());
            Assert.fail("Test failed due to an unexpected exception: " + e.getMessage());
        }

        // --- Demonstration 3: Generic FrameworkException (for unrecoverable errors) ---
        // This could be used for configuration issues, setup failures, etc.
        TestListener.getExtentTest().info("Simulating a generic FrameworkException.");
        try {
            // Let's imagine a critical configuration was missing.
            boolean criticalConfigMissing = true; // Set to false to see test pass
            if (criticalConfigMissing) {
                TestListener.getExtentTest().fail("Simulating critical framework configuration missing.");
                throw new FrameworkException("Critical 'api.endpoint.url' configuration property is missing or invalid.");
            }
            TestListener.getExtentTest().pass("Simulated critical config check passed.");
        } catch (FrameworkException fe) {
            TestListener.getExtentTest().fail("Caught expected FrameworkException: " + fe.getMessage());
            Assert.fail("Test failed due to Framework Exception: " + fe.getMessage());
        }


        TestListener.getExtentTest().info("Custom exception demonstrations concluded.");
        // Note: Because we are throwing exceptions in the try-catch blocks and then asserting fail,
        // the test will likely show as failed in the report, which is expected for a demonstration of error handling.
        // For a passing test, the try-catch blocks would simply log the custom exception and not re-throw/fail.
    }
}
