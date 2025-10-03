package com.sauceDemo.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePageFunctions {

    @FindBy(id = "user-name")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = ".error-message-container.error")
    private WebElement errorMessageContainer;

    private By errorMessageLocator = By.cssSelector("h3[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
        System.out.println("LoginPage initialized.");
    }

    public LoginPage enterUsername(String username) {
        typeText(usernameField, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        typeText(passwordField, password);
        return this;
    }

    public ProductsPage clickLoginButtonSuccess() {
        clickElement(loginButton);
        System.out.println("Clicked login button, expecting success.");
        // Wait for products page to load after successful login
        wait.until(ExpectedConditions.urlContains("/inventory.html"));
        return new ProductsPage(driver); // Navigates to ProductsPage
    }

    public LoginPage clickLoginButtonFailure() {
        clickElement(loginButton);
        System.out.println("Clicked login button, expecting failure.");
        // Wait for the error message to be visible after failed login
        wait.until(ExpectedConditions.visibilityOf(errorMessageContainer));
        return this; // Stays on LoginPage
    }

    // Method to check if error message is displayed
    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(errorMessageContainer);
    }

    // Method to get the text of the error message
    public String getErrorMessageText() {
        return getElementText(errorMessageContainer);
    }

    public String getErrorMessage() {
        WebElement errorMessageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessageLocator));
        return getElementText(errorMessageElement);
    }

    public boolean isLoginButtonDisplayed() {
        return isElementDisplayed(loginButton);
    }


}