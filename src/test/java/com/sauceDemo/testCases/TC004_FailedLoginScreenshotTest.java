package com.sauceDemo.testCases;

import com.sauceDemo.bases.BaseTest;
import com.sauceDemo.pageObjects.LoginPage;
import com.sauceDemo.utils.ConfigLoader;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC004_FailedLoginScreenshotTest extends BaseTest {

    @Test(priority = 1, description = "Verify that a screenshot is taken on failed login due to incorrect credentials")
    public void testInvalidLoginForScreenshot() {
        getDriver().get(ConfigLoader.getProperty("base.url"));
        LoginPage loginPage = new LoginPage(getDriver());

        // This login is designed to fail and trigger a screenshot
        loginPage.enterUsername("nonexistent_user")
                .enterPassword("wrong_password")
                .clickLoginButtonFailure();

        // This assertion will fail, triggering the TestListener to take a screenshot
        String expectedErrorMessage = "Epic sadface: This is an incorrect error message to force failure"; // Intentionally incorrect
        Assert.assertEquals(loginPage.getErrorMessage(), expectedErrorMessage, "Error message for invalid login is incorrect.");
    }

    @Test(priority = 2, description = "Verify that a screenshot is taken when a locked out user tries to login")
    public void testLockedOutUserLoginForScreenshot() {
        getDriver().get(ConfigLoader.getProperty("base.url"));
        LoginPage loginPage = new LoginPage(getDriver());

        // This login uses a locked out user, which results in a specific error
        // We'll intentionally assert something wrong to trigger a screenshot
        loginPage.enterUsername(ConfigLoader.getProperty("locked.out.user.username"))
                .enterPassword(ConfigLoader.getProperty("locked.out.user.password"))
                .clickLoginButtonFailure();

        String expectedErrorMessage = "Epic sadface: Intentional wrong message for screenshot"; // Intentionally incorrect
        Assert.assertEquals(loginPage.getErrorMessage(), expectedErrorMessage, "Error message for locked out user is incorrect.");
    }
}