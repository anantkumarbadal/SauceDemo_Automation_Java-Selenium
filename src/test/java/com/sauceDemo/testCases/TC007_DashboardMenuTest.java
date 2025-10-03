package com.sauceDemo.testCases;

import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.sauceDemo.bases.BaseTest;
import com.sauceDemo.testngUtils.TestListener; // Your TestListener
import com.sauceDemo.pageObjects.LoginPage; // Your existing LoginPage POM
import com.sauceDemo.pageObjects.DashboardPage; // Your new DashboardPage POM
import com.sauceDemo.pageObjects.ProductsPage; // Your existing ProductsPage POM (returned after login)
import com.sauceDemo.utils.ConfigLoader; // Your ConfigLoader

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TC007_DashboardMenuTest extends BaseTest {

    @Test(description = "Verify all items and their text in the Dashboard Hamburger Menu.")
    public void verifyHamburgerMenuItems() {

        LoginPage loginPage = new LoginPage(getDriver());

        String baseUrl = ConfigLoader.getProperty("base.url");
        String standardUsername = ConfigLoader.getProperty("standard.user.username");
        String standardPassword = ConfigLoader.getProperty("standard.user.password");

        // Get expected menu items from ConfigLoader and parse the comma-separated string
        String menuItemsConfig = ConfigLoader.getProperty("dashboard.hamburger.menu.items");
        List<String> expectedMenuItemNames = Arrays.stream(menuItemsConfig.split(","))
                .map(String::trim) // Remove leading/trailing spaces
                .collect(Collectors.toList());

        // --- Step 2: Pre-condition - Log in using fluent interface and log details ---
        TestListener.getExtentTest().info("<b>Pre-condition:</b> Navigating to base URL: " + ConfigLoader.getProperty("base.url"));
        getDriver().get(ConfigLoader.getProperty("base.url"));

        TestListener.getExtentTest().info("Attempting login with user: <b>" + standardUsername + "</b>.");


        ProductsPage productsPage = loginPage.enterUsername(standardUsername)
                .enterPassword(standardPassword)
                .clickLoginButtonSuccess();

        TestListener.getExtentTest().pass("Successfully logged in and landed on Products page.");

        // --- Step 3: Instantiate DashboardPage after successful login ---
        // Dashboard elements become accessible after successful login onto the ProductsPage
        DashboardPage dashboardPage = new DashboardPage(getDriver());


        // --- Step 4: Action - Open Hamburger Menu using DashboardPage POM ---
        TestListener.getExtentTest().info("Clicking on the Hamburger menu icon using DashboardPage POM.");
        dashboardPage.openHamburgerMenu();
        TestListener.getExtentTest().info("Hamburger menu opened successfully.");

        // --- Step 5: Validation - Retrieve and Verify Menu Items ---
        TestListener.getExtentTest().info("Retrieving all menu items from the Hamburger menu via DashboardPage POM.");
        List<String> actualMenuItemNames = dashboardPage.getHamburgerMenuItems();

        // Log both expected and actual items clearly in the report for analysis
        // Pass the string and MarkupHelper object in separate info calls
        TestListener.getExtentTest().info("<b>Expected Menu Items:</b>");
        TestListener.getExtentTest().info(MarkupHelper.createOrderedList(expectedMenuItemNames));

        TestListener.getExtentTest().info("<b>Actual Menu Items Found:</b>");
        TestListener.getExtentTest().info(MarkupHelper.createOrderedList(actualMenuItemNames));

        // Perform the assertion to compare lists
        Assert.assertEquals(actualMenuItemNames, expectedMenuItemNames,
                "Hamburger menu items do not match the expected list.");

        // --- Step 6: Detailed PASS Message ---
        // This message explains *what* passed and provides context.
        TestListener.getExtentTest().pass("<b>Validation Passed:</b> Successfully verified all " + actualMenuItemNames.size() +
                " Hamburger menu items. All items match the expected list, as displayed above.");

        // --- Step 7: Optional - Close the menu ---
        TestListener.getExtentTest().info("Closing the Hamburger menu.");
        dashboardPage.closeHamburgerMenu();
    }
}