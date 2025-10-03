package com.sauceDemo.testCases;

import com.sauceDemo.bases.BaseTest;
import com.sauceDemo.pageObjects.LoginPage;
import com.sauceDemo.pageObjects.ProductsPage;
import com.sauceDemo.utils.ConfigLoader;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC005_RetryAnalyzerTest extends BaseTest {

    // A static counter to simulate a test that fails a few times then passes.
    // In a real scenario, flakiness is usually due to timing/element issues,
    // not explicit counters like this. This is purely for demonstration.
    private static int retryAttemptCount = 0;
    // Note: If you run tests in parallel, this static counter might behave unexpectedly
    // across different test methods. For demonstration of a *single* flaky test, it's fine.
    // For more advanced retry logic with parallel tests, you'd need a ThreadLocal<Integer> or
    // pass data via ITestResult attributes. For this simple demo, static is clear.

    @Test(priority = 1, description = "Demonstrates RetryAnalyzer functionality - designed to fail then pass")
    public void testFlakyLoginAttempt() {
        System.out.println("Running testFlakyLoginAttempt - Attempt: " + (retryAttemptCount + 1));

        getDriver().get(ConfigLoader.getProperty("base.url"));
        LoginPage loginPage = new LoginPage(getDriver());

        // Always perform the login action
        ProductsPage productsPage = loginPage.enterUsername(ConfigLoader.getProperty("standard.user.username"))
                .enterPassword(ConfigLoader.getProperty("standard.user.password"))
                .clickLoginButtonSuccess();

        // Check if products header is displayed (standard assertion)
        Assert.assertTrue(productsPage.isProductsHeaderDisplayed(), "Products header should be displayed after login.");
        Assert.assertEquals(productsPage.getProductsHeaderText(), "Products", "Products header text should be 'Products'.");

        // --- SIMULATE FLAKINESS ---
        // This assertion will fail on the first two attempts (0 and 1)
        // and pass on the third attempt (when retryAttemptCount becomes 2 or more).
        // Since MAX_RETRY_COUNT is 2, it means original + 2 retries = 3 attempts total.
        if (retryAttemptCount < 2) { // Change this number to control how many times it fails
            retryAttemptCount++; // Increment for next retry
            System.out.println("Intentional failure on attempt " + retryAttemptCount + " for testFlakyLoginAttempt.");
            Assert.fail("Simulating a transient failure. This test should be retried!");
        } else {
            System.out.println("TestFlakyLoginAttempt: Successfully passed after previous retries.");
            // Reset for future test runs if needed in same suite (careful with parallel)
            retryAttemptCount = 0;
        }
        // --- END SIMULATION ---
    }
}

/*
Important Note on static int retryAttemptCount:
This static counter is purely for a simple demonstration. In a real-world scenario,
you would not use a static counter within your test class like this for flakiness,
especially if running many tests in parallel. Real flakiness is usually due to
 application behavior or timing, and the RetryAnalyzer would handle it automatically
 without needing to manipulate a counter in the test method itself.
  The RetryAnalyzer gets a fresh instance for each test method invocation.
 */