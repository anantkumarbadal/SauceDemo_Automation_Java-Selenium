package com.sauceDemo.testsPractice;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions; // For visibility of elements
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Set;

public class HyundaiFooterWindowTest {


    private WebDriver driver; // WebDriver instance for this test class

    @BeforeClass
    public void setupDriver() {
        // Setup WebDriver for this independent test class
        // Ensure you have ChromeDriver installed and its path accessible to your system
        // Or use WebDriverManager: WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        // Implicit wait applies to element finding, not necessarily page loads or new windows
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        System.out.println("Hyundai Test: WebDriver initialized.");
    }

    @AfterClass
    public void tearDownDriver() {
        if (driver != null) {
            System.out.println("Hyundai Test: Quitting WebDriver.");
            driver.quit();
        }
    }

    @Test(description = "Validate Hyundai Warranty link opens correctly in a new tab.")
    public void testHyundaiWarrantyLinkInNewTab() throws InterruptedException {
        String hyundaiOwnersUrl = "https://owners.hyundaiusa.com/us/en";
        String expectedWarrantyTitle = "Hyundai Warranty Coverage | America's Best | Hyundai USA";
        String expectedWarrantyUrl = "https://www.hyundaiusa.com/us/en/assurance/america-best-warranty";

        System.out.println("Hyundai Test: Navigating to Hyundai Owners Portal: " + hyundaiOwnersUrl);
        driver.get(hyundaiOwnersUrl);
        System.out.println("Hyundai Test: Navigated to Hyundai Owners Portal.");

        // --- Step 1: Accept Cookies ---
        System.out.println("Hyundai Test: Attempting to accept cookies.");
        try {
            // Wait for the cookie banner to appear and "Accept All" button to be clickable
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement acceptCookiesButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(., 'Accept All')]")));
            acceptCookiesButton.click();
            System.out.println("Hyundai Test: Cookies accepted.");
            // Wait briefly for the banner to disappear if it blocks elements
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("onetrust-accept-btn-handler")));
        } catch (Exception e) {
            System.out.println("Hyundai Test: Cookie banner not found or not clickable within timeout, proceeding without accepting. (Error: " + e.getMessage() + ")");
            // Proceed anyway if cookie banner is not critical for test flow
        }

        // --- Step 2: Scroll down to bottom footer to make Warranty link visible ---
        System.out.println("Hyundai Test: Scrolling to bottom of the page.");
        HelperUtils.executeJavaScript(driver, "window.scrollTo(0, document.body.scrollHeight);");
        // Add a small wait for scroll to complete and elements to render
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Hyundai Test: Scrolled to bottom.");


        // --- Step 3: Capture initial window handles BEFORE clicking the link ---
        String originalWindowHandle = driver.getWindowHandle();
        Set<String> initialWindowHandles = driver.getWindowHandles();
        System.out.println("DEBUG Hyundai Test: Original window handle: " + originalWindowHandle);
        System.out.println("DEBUG Hyundai Test: Initial number of windows: " + initialWindowHandles.size());

        // --- Step 4: Click on 'Warranty' link ---
        System.out.println("Hyundai Test: Attempting to find and click 'Warranty' link.");
        WebElement warrantyLink = driver.findElement(By.xpath("//a[contains(text(),'Warranty')]"));
        // Scroll to the element to ensure it's in view before clicking
        HelperUtils.executeJavaScript(driver, "arguments[0].scrollIntoView(true);", warrantyLink);
        // Add a small wait after scrolling to ensure clickability
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        warrantyLink.click();
        System.out.println("Hyundai Test: Clicked 'Warranty' link.");

        // --- Step 5: Use helper method to switch to the new tab/window ---
        System.out.println("Hyundai Test: Attempting to switch to the new tab/window.");
        String newWindowHandle = HelperUtils.switchToFirstNewlyOpenedWindow(driver, initialWindowHandles, 15); // Increased timeout for external page

        if (newWindowHandle != null) {
            // Switch to the new window using the handle returned by the helper method
            driver.switchTo().window(newWindowHandle);
            System.out.println("Hyundai Test: Successfully switched to the new tab/window.");

            Thread.sleep(3000);

            // --- Step 6: Validate New Tab/Window (Title, URL, Content) ---
            String actualNewWindowTitle = driver.getTitle();
            String actualNewWindowUrl = driver.getCurrentUrl();

            System.out.println("Hyundai Test: New Tab Title: " + actualNewWindowTitle);
            System.out.println("Hyundai Test: New Tab URL: " + actualNewWindowUrl);

            Assert.assertEquals(actualNewWindowTitle, expectedWarrantyTitle, "New tab title mismatch.");
            System.out.println("Hyundai Test: Validated Title: '" + actualNewWindowTitle + "' matches expected.");

            Assert.assertEquals(actualNewWindowUrl, expectedWarrantyUrl, "New tab URL mismatch.");
            System.out.println("Hyundai Test: Validated URL: '" + actualNewWindowUrl + "' matches expected.");

            //accepting cookies
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            WebElement acceptCookies = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(., 'Accept All')]")));
            acceptCookies.click();
            System.out.println("New Window: Cookies accepted");


            //entering PINCODE in the Popup
            WebElement textbox_ZIP = driver.findElement(By.id("zipModalInput"));
            textbox_ZIP.sendKeys("92708");
            System.out.println("ZipCode Entered");
            Thread.sleep(1000);
            WebElement submitButton = driver.findElement(By.xpath("//div[@class='zip-modal-buttons']//button[contains(text(),'Confirm')]"));
            submitButton.click();
            System.out.println("Submit Button Clicked");


            // Validate if the page correctly loaded (e.g., scroll and check footer element)
            System.out.println("Hyundai Test: Scrolling down the new tab to check content load.");
            HelperUtils.executeJavaScript(driver, "window.scrollTo(0, document.body.scrollHeight);");
            System.out.println("Scrolled down to bottom of the Page to get the copyright text validation");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } // Give it time to load after scroll

            try {

                WebElement footerCopyright = driver.findElement(By.xpath("//div[@class='footer-copy']//p[2]"));
                System.out.println("Copyright text: " + footerCopyright.getText());
                Assert.assertTrue(footerCopyright.isDisplayed(), "Footer copyright element not displayed, page might not have loaded correctly.");
                System.out.println("Hyundai Test: Page content appears loaded (footer element found).");
            } catch (Exception e) {
                System.err.println("Hyundai Test: Failed to verify content on new page after scroll: " + e.getMessage());
                Assert.fail("Hyundai Test: Page did not load correctly or expected content not found.");
            }

            // --- Step 7: Close the new tab/window ---
            System.out.println("Hyundai Test: Closing the new tab/window.");
            driver.close();
            System.out.println("Hyundai Test: New tab/window closed.");

            // --- Step 8: Switch back to the original tab/window ---
            System.out.println("Hyundai Test: Switching back to the original tab/window.");
            HelperUtils.switchToOriginalWindow(driver, originalWindowHandle);
            System.out.println("Hyundai Test: Switched back to original tab/window.");

            // --- Step 9: Verify being back on the original page ---
            System.out.println("Hyundai Test: Verifying current URL after switching back.");
            Assert.assertEquals(driver.getCurrentUrl(), hyundaiOwnersUrl, "Should be back on Hyundai Owners page.");
            System.out.println("Hyundai Test: Successfully verified return to original Hyundai Owners page.");

        } else {
            System.err.println("Hyundai Test: Failed to switch to a new tab/window within the timeout.");
            Assert.fail("Hyundai Test: Could not switch to new tab/window.");
        }

        System.out.println("Hyundai Test: Hyundai Warranty link test complete.");
    }













}
