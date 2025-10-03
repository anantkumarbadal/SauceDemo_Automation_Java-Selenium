package com.sauceDemo.testCases;

import com.sauceDemo.bases.BaseTest; // Extend BaseTest
import com.sauceDemo.testngUtils.TestListener; // Import your TestListener
import com.sauceDemo.pageObjectHyundai.HyundaiOwnersPage; // Import the new Hyundai Page Object
import com.sauceDemo.utils.WebDriverUtils; // Import WebDriverUtils
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;

import java.util.Set; // For window handles

public class TC013_HyundaiWarrantyWindowHandling extends BaseTest { // Extends BaseTest

    @Test(description = "Verify Hyundai Warranty link opens correctly in a new tab using framework utilities.")
    public void testHyundaiWarrantyLinkWindowHandling() {

        // --- Step 1: Initialize Hyundai Page Object and Navigate ---
        HyundaiOwnersPage hyundaiPage = new HyundaiOwnersPage(getDriver());

        String hyundaiOwnersUrl = "https://owners.hyundaiusa.com/us/en";
        String expectedWarrantyTitle = "Hyundai Warranty Coverage | America's Best | Hyundai USA";
        String expectedWarrantyUrl = "https://www.hyundaiusa.com/us/en/assurance/america-best-warranty";
        String expectedFooterCopyrightDisclaimerText = "Hyundai is a registered trademark of Hyundai Motor Company. All rights reserved. ©2025 Hyundai Motor America.";

        TestListener.getExtentTest().info("Navigating to Hyundai Owners Portal: " + hyundaiOwnersUrl);
        getDriver().get(hyundaiOwnersUrl); // Use BaseTest's getDriver()
        TestListener.getExtentTest().pass("Navigated to Hyundai Owners Portal.");

        // --- Step 2: Accept Cookies ---
        TestListener.getExtentTest().info("Attempting to accept cookies on Hyundai portal.");
        Assert.assertTrue(hyundaiPage.isCookiesWindowDisplayed(), "Cookie Window is not displayed");
        hyundaiPage.acceptCookies();
        TestListener.getExtentTest().pass("Cookie handling attempted.");

        // --- Step 3: Capture initial window handles BEFORE clicking the link ---
        String originalWindowHandle = getDriver().getWindowHandle();
        Set<String> initialWindowHandles = getDriver().getWindowHandles();
        TestListener.getExtentTest().info("Original window handle: " + originalWindowHandle);
        TestListener.getExtentTest().info("Initial number of windows: " + initialWindowHandles.size());

        // --- Step 4: Scroll to and Click 'Warranty' link ---
        TestListener.getExtentTest().info("Scrolling to and clicking 'Warranty' link.");
        WebDriverUtils.scrollToElement(getDriver(), hyundaiPage.warrantyLink);
        hyundaiPage.clickOnWarrantyLink();
        TestListener.getExtentTest().pass("Clicked 'Warranty' link.");

        // --- Step 5: Use WebDriverUtils to switch to the new tab/window ---
        TestListener.getExtentTest().info("Using WebDriverUtils to switch to the new tab/window.");
        String newWindowHandle = WebDriverUtils.switchToFirstNewlyOpenedWindow(getDriver(), initialWindowHandles, 20); // Pass BaseTest's driver and initial handles

        if (newWindowHandle != null) {

            // Switch BaseTest's driver to the new window using the handle returned by the helper
            getDriver().switchTo().window(newWindowHandle);
            TestListener.getExtentTest().pass("Successfully switched to the new tab/window.");

            //Wait for Page to get loaded
            WebDriverUtils.waitForPageLoad(getDriver(),30);

            // --- Step 6: Validate New Tab/Window (Title, URL, Content) ---
            String actualNewWindowTitle = getDriver().getTitle();
            String actualNewWindowUrl = getDriver().getCurrentUrl();

            TestListener.getExtentTest().info("New Tab Title: <b>" + actualNewWindowTitle + "</b>");
            TestListener.getExtentTest().info("New Tab URL: " + actualNewWindowUrl);

            Assert.assertEquals(actualNewWindowTitle, expectedWarrantyTitle, "New tab title mismatch.");
            TestListener.getExtentTest().pass("Validated Title: '" + actualNewWindowTitle + "' matches expected.");

            Assert.assertEquals(actualNewWindowUrl, expectedWarrantyUrl, "New tab URL mismatch.");
            TestListener.getExtentTest().pass("Validated URL: '" + actualNewWindowUrl + "' matches expected.");

            // Validate if the page correctly loaded using the POM's method
            TestListener.getExtentTest().info("Checking if the new tab content loaded correctly (footer element).");
            HyundaiOwnersPage newTabHyundaiPage = new HyundaiOwnersPage(getDriver()); // Re-init POM for new context

            //check cookie
            TestListener.getExtentTest().info("Handling Cookie in New Window: ");
            newTabHyundaiPage.closeCookieWindow();
            TestListener.getExtentTest().pass("Cookie handling attempted.");

            //check zipcode text box and click
            TestListener.getExtentTest().info("Handling Zipcode in New Window");
            Assert.assertTrue(newTabHyundaiPage.isZipCodeTextBoxDisplayed(), "Zipcode window is not displayed");
            newTabHyundaiPage.enterValueInZipCodeTextbox("92708");
            newTabHyundaiPage.clickOnConfirmButton();
            TestListener.getExtentTest().pass("Entered Zipcode input");

            // --- Step 4: Scroll to Footer---
            TestListener.getExtentTest().info("Scrolling to Footer");
            WebDriverUtils.scrollToElement(getDriver(), newTabHyundaiPage.warrantyPageFooterCopyright);
            Assert.assertTrue(newTabHyundaiPage.isWarrantyPageFooterDisplayed(), "New tab content did not load correctly or expected footer not found.");
            TestListener.getExtentTest().pass("Scrolled to Footer");

            //Footer Disclaimer
            String actualFooterCopyrightDisclaimerText = hyundaiPage.getFooterCopyrightText();
            TestListener.getExtentTest().info("Validating Footer Copyright disclaimer");
            Assert.assertEquals(actualFooterCopyrightDisclaimerText,expectedFooterCopyrightDisclaimerText, "Footer Copyright text not matching" );
            TestListener.getExtentTest().pass("Validated Footer Copyright disclaimer text: '" + actualFooterCopyrightDisclaimerText + "' matches expected.");

            // --- Step 7: Close the new tab/window ---
            TestListener.getExtentTest().info("Closing the new tab/window.");
            getDriver().close(); // Closes the currently focused window (the new one)
            TestListener.getExtentTest().pass("New tab/window closed.");

            // --- Step 8: Switch back to the original tab/window ---
            TestListener.getExtentTest().info("Switching back to the original tab/window.");
            WebDriverUtils.switchToOriginalWindow(getDriver(), originalWindowHandle); // Pass BaseTest's driver
            TestListener.getExtentTest().pass("Switched back to original tab/window.");

            // --- Step 9: Verify being back on the original page ---
            TestListener.getExtentTest().info("Verifying current URL after switching back.");
            Assert.assertEquals(getDriver().getCurrentUrl(), hyundaiOwnersUrl, "Should be back on Hyundai Owners page.");
            TestListener.getExtentTest().pass("Successfully verified return to original Hyundai Owners page.");

        } else {
            TestListener.getExtentTest().fail("Failed to switch to a new tab/window within the timeout.");
            Assert.fail("Test failed: Could not switch to new tab/window.");
        }

        TestListener.getExtentTest().pass("Hyundai Warranty link window handling test complete.");
    }


}
