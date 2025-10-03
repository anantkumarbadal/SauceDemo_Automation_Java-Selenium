package com.sauceDemo.testsPractice;

// No package declaration needed if you put it directly in src/test/java/
// If you put it in a package like 'com.example.tests', then: package com.example.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class serves as a standalone test to verify the window handling logic.
 * It does NOT extend BaseTest and manages its own WebDriver lifecycle.
 * It contains a simplified, nested WebDriverUtils for demonstration.
 */
public class StandaloneWindowTest {

    private WebDriver driver; // WebDriver instance for this test class

    @BeforeClass
    public void setupDriver() {
        // Setup WebDriver for this independent test class
        // You might use WebDriverManager here if configured, e.g.:
        // WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5)); // Small implicit wait
        System.out.println("Standalone Test: WebDriver initialized.");
    }

    @AfterClass
    public void tearDownDriver() {
        if (driver != null) {
            System.out.println("Standalone Test: Quitting WebDriver.");
            driver.quit();
        }
    }

    @Test(description = "Verify window handling logic on DemoQA portal in a standalone setup.")
    public void testWindowHandlingStandalone() {
        String demoQaUrl = "https://demoqa.com/browser-windows";
        System.out.println("Standalone Test: Navigating to DemoQA Browser Windows URL: " + demoQaUrl);
        driver.get(demoQaUrl);
        System.out.println("Standalone Test: Navigated to DemoQA Browser Windows page.");

        String originalWindowHandle = driver.getWindowHandle();
        Set<String> initialWindowHandles = driver.getWindowHandles();
        System.out.println("DEBUG Standalone: Original window handle: " + originalWindowHandle);
        System.out.println("DEBUG Standalone: Initial number of windows: " + initialWindowHandles.size());

        // --- Identify the button that opens a new window ---
        WebElement newWindowButton = driver.findElement(By.id("windowButton"));
        System.out.println("Standalone Test: Clicking 'New Window' button.");
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", newWindowButton);
        newWindowButton.click();
        System.out.println("Standalone Test: Clicked 'New Window' button.");

        // --- Use the standalone WebDriverUtils to switch to the new window ---
        System.out.println("Standalone Test: Attempting to switch to the first newly opened window.");
        WebDriver newWindowDriver = StandaloneWebDriverUtils.switchToFirstNewlyOpenedWindow(driver, initialWindowHandles, 10); // 10 seconds timeout

        if (newWindowDriver != null) {
            System.out.println("Standalone Test: Successfully switched to the new window.");
            String newWindowTitle = newWindowDriver.getTitle();
            String newWindowUrl = newWindowDriver.getCurrentUrl();
            System.out.println("Standalone Test: New Window Title: " + newWindowTitle);
            System.out.println("Standalone Test: New Window URL: " + newWindowUrl);

            // --- Verify content in the new window ---
            try {
                WebElement samplePageHeading = newWindowDriver.findElement(By.id("sampleHeading"));
                String headingText = samplePageHeading.getText();
                System.out.println("Standalone Test: Found heading in new window: '" + headingText + "'");
                Assert.assertEquals(headingText, "This is a sample page", "Heading text in new window mismatch.");
                System.out.println("Standalone Test: Verified content in the new window.");
            } catch (Exception e) {
                System.err.println("Standalone Test: Failed to verify content in new window: " + e.getMessage());
                Assert.fail("Standalone Test: Failed to find expected content in new window.");
            }

            // --- Close the new window ---
            System.out.println("Standalone Test: Closing the new window.");
            newWindowDriver.close();
            System.out.println("Standalone Test: New window closed.");

            // --- Switch back to the original window ---
            System.out.println("Standalone Test: Switching back to the original window.");
            StandaloneWebDriverUtils.switchToOriginalWindow(driver, originalWindowHandle);
            System.out.println("Standalone Test: Switched back to original window.");

            // --- Verify being back on the original page ---
            System.out.println("Standalone Test: Verifying current URL after switching back.");
            Assert.assertEquals(driver.getCurrentUrl(), demoQaUrl, "Should be back on DemoQA page.");
            System.out.println("Standalone Test: Successfully verified return to original DemoQA page.");

        } else {
            System.err.println("Standalone Test: Failed to open or switch to a new window within the timeout.");
            Assert.fail("Standalone Test: Could not open or switch to new window.");
        }

        System.out.println("Standalone Test: Window handling demonstration complete.");
    }

    /**
     * Simplified WebDriverUtils for this standalone test.
     * This class contains only the necessary window handling methods for this test.
     */
    static class StandaloneWebDriverUtils {

        /**
         * Checks if a new window handle has appeared by comparing current handles to initial handles.
         * @param initialHandles The set of window handles present before the new window opened.
         * @param currentHandles The current set of window handles.
         * @return True if a new window handle is detected, false otherwise.
         */
        private static boolean isNewWindowDetected(Set<String> initialHandles, Set<String> currentHandles) {
            Set<String> newlyOpenedHandles = new HashSet<>(currentHandles);
            newlyOpenedHandles.removeAll(initialHandles);
            return !newlyOpenedHandles.isEmpty();
        }

        /**
         * Switches to the first new window/tab that appears after an action that opens a new window.
         * This method waits for a new window to open, identifies it, and switches to it.
         *
         * @param driver The WebDriver instance.
         * @param initialWindowHandles The set of window handles present *before* the action that opened the new window.
         * @param timeoutInSeconds The maximum time to wait for a new window to appear.
         * @return The WebDriver instance focused on the new window, or null if no new window appears within the timeout.
         */
        public static WebDriver switchToFirstNewlyOpenedWindow(WebDriver driver, Set<String> initialWindowHandles, int timeoutInSeconds) {
            System.out.println("DEBUG Standalone WU: Entered switchToFirstNewlyOpenedWindow. Initial handles count (from caller): " + initialWindowHandles.size());

            long startTime = System.currentTimeMillis();
            long endTime = startTime + (timeoutInSeconds * 1000L);

            WebDriver newWindowDriver = null;

            while (System.currentTimeMillis() < endTime) {
                Set<String> currentHandles = driver.getWindowHandles();
                System.out.println("DEBUG Standalone WU: Polling... Current handles count: " + currentHandles.size());

                if (isNewWindowDetected(initialWindowHandles, currentHandles)) {
                    Set<String> newlyFoundHandles = new HashSet<>(currentHandles);
                    newlyFoundHandles.removeAll(initialWindowHandles);

                    if (!newlyFoundHandles.isEmpty()) {
                        String newWindowHandle = newlyFoundHandles.iterator().next();
                        driver.switchTo().window(newWindowHandle);
                        newWindowDriver = driver;
                        System.out.println("DEBUG Standalone WU: Successfully switched to new window with handle: " + newWindowHandle);
                        System.out.println("DEBUG Standalone WU: New window title: " + driver.getTitle());
                        return newWindowDriver;
                    }
                }

                try {
                    Thread.sleep(500); // Poll every 500 milliseconds
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Standalone WU: Thread interrupted during window wait.");
                    return null;
                }
            }
            System.err.println("Standalone WU: Timeout waiting for a new window to appear after " + timeoutInSeconds + " seconds.");
            return null;
        }

        /**
         * Switches back to the original window/tab.
         * @param driver The WebDriver instance.
         * @param originalWindowHandle The handle of the window to switch back to.
         */
        public static void switchToOriginalWindow(WebDriver driver, String originalWindowHandle) {
            driver.switchTo().window(originalWindowHandle);
            System.out.println("DEBUG Standalone WU: Switched back to original window: " + originalWindowHandle);
        }
    }
}