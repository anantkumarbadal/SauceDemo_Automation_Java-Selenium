package com.sauceDemo.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CheckoutInformationPage extends BasePageFunctions {

    @FindBy(css = ".title")
    private WebElement checkoutHeader;

    @FindBy(id = "first-name")
    private WebElement firstNameField;

    @FindBy(id = "last-name")
    private WebElement lastNameField;

    @FindBy(id = "postal-code")
    private WebElement postalCodeField;

    @FindBy(id = "continue")
    private WebElement continueButton;

    @FindBy(id = "cancel")
    private WebElement cancelButton;

    public CheckoutInformationPage(WebDriver driver) {
        super(driver);
        System.out.println("CheckoutInformationPage initialized.");
    }

    public boolean isCheckoutHeaderDisplayed() {
        return isElementDisplayed(checkoutHeader);
    }

    public String getCheckoutHeaderText() {
        return getElementText(checkoutHeader);
    }

    public CheckoutInformationPage enterYourInformation(String firstName, String lastName, String postalCode) {
        typeText(firstNameField, firstName);
        typeText(lastNameField, lastName);
        typeText(postalCodeField, postalCode);
        System.out.println("Entered checkout information.");
        return this;
    }

    public CheckoutOverviewPage clickContinue() {
        clickElement(continueButton);
        System.out.println("Clicked continue button on checkout information page.");
        return new CheckoutOverviewPage(driver); // Navigates to CheckoutOverviewPage
    }

    public CartPage clickCancel() {
        clickElement(cancelButton);
        System.out.println("Clicked cancel button on checkout information page.");
        return new CartPage(driver); // Navigates back to CartPage
    }

    @FindBy(css = ".error-message-container.error") // Common locator for error messages on SauceDemo
    private WebElement errorMessageContainer;

    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(errorMessageContainer); // Use BasePage method
    }

    public String getErrorMessageText() {
        if (isErrorMessageDisplayed()) {
            return getElementText(errorMessageContainer).trim(); // Use BasePage method
        }
        return null;
    }


}