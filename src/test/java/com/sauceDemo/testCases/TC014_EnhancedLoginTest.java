package com.sauceDemo.testCases;

import com.sauceDemo.bases.BaseTest;
import com.sauceDemo.utils.SmartElement;
import io.qameta.allure.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Login Features")
@Feature("User Authentication")
public class TC014_EnhancedLoginTest extends BaseTest {
    private static final Logger logger = LogManager.getLogger(TC014_EnhancedLoginTest.class);

    // Smart elements with alternative locators for self-healing
    private final By usernameLocator = By.id("user-name");
    private final By usernameLocatorAlt = By.cssSelector("input[data-test='username']");
    private final By passwordLocator = By.id("password");
    private final By loginButtonLocator = By.id("login-button");
    private final By productsHeaderLocator = By.className("title");

    @Test(description = "Verify login with enhanced framework features")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Login with valid credentials")
    @Description("Test login functionality with smart element handling and detailed logging")
    public void testEnhancedLogin() {
        logger.info("Starting enhanced login test");

        // Using SmartElement with self-healing capabilities
        SmartElement usernameField = new SmartElement(getDriver(), usernameLocator, usernameLocatorAlt);
        SmartElement passwordField = new SmartElement(getDriver(), passwordLocator);
        SmartElement loginButton = new SmartElement(getDriver(), loginButtonLocator);
        SmartElement productsHeader = new SmartElement(getDriver(), productsHeaderLocator);

        try {
            // Test execution with detailed logging and timing
            logger.info("Navigating to login page");
            getDriver().get("https://www.saucedemo.com");

            logger.info("Entering username");
            usernameField.sendKeys("standard_user");

            logger.info("Entering password");
            passwordField.sendKeys("secret_sauce");

            logger.info("Clicking login button");
            loginButton.click();

            logger.info("Verifying successful login");
            Assert.assertTrue(productsHeader.isDisplayed(), "Products page header should be visible after login");
            Assert.assertEquals(productsHeader.getText(), "Products", "Header text should match expected value");

            logger.info("Login test completed successfully");
        } catch (Exception e) {
            logger.error("Login test failed", e);
            throw e;
        }
    }
}
