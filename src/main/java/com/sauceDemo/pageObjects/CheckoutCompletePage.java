package com.sauceDemo.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutCompletePage extends BasePageFunctions {

    @FindBy(css = ".title")
    private WebElement completeHeader;

    @FindBy(className = "complete-text")
    private WebElement completeText;

    @FindBy(className = "pony_express")
    private WebElement ponyExpressImage;

    @FindBy(id = "back-to-products")
    private WebElement backToHomeButton;


    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
        System.out.println("CheckoutCompletePage initialized.");
    }

    public boolean isCompleteHeaderDisplayed() {
        return isElementDisplayed(completeHeader);
    }

    public String getCompleteHeaderText() {
        return getElementText(completeHeader);
    }

    public String getCompleteDispatchMessageText() {
        return getElementText(completeText).trim();
    }

    public boolean isCompleteDispatchMessageDisplayed(){
        return isElementDisplayed(completeText);
    }

    public boolean isBackHomeButtonEnabled()
    {
        return isElementEnabled(backToHomeButton);
    }

    public ProductsPage clickBackToHome() {
        clickElement(backToHomeButton);
        System.out.println("Clicked back to products button.");

        // Wait for products page to load after going back to Dashboard
        wait.until(ExpectedConditions.urlContains("/inventory.html"));
        return new ProductsPage(driver);
    }

    // Locator for "THANK YOU FOR YOUR ORDER" header
    @FindBy(className = "complete-header")
    private WebElement completeHeaderThankYou;

    public String getConfirmationMessageText() {
        return getElementText(completeHeaderThankYou).trim();
    }

    public boolean isOrderCompleteMessageDisplayed() {
        return isElementDisplayed(completeHeaderThankYou);
    }







}