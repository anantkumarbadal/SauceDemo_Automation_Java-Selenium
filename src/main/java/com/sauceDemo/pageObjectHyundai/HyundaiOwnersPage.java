package com.sauceDemo.pageObjectHyundai;

import com.sauceDemo.pageObjects.BasePageFunctions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HyundaiOwnersPage extends BasePageFunctions {

//onetrust-accept-btn-handler
    @FindBy(xpath = "//*[@id='onetrust-accept-btn-handler' or @id='onetrust-close-btn-container']") // Cookie "Accept All" button or Close button in new window
    private WebElement acceptAllCookiesButton;

    @FindBy(xpath = "//a[contains(text(),'Warranty')]") // Footer Warranty link
    public WebElement warrantyLink;

    @FindBy(id = "zipModalInput") // Input textbox
    public WebElement zipcode_textbox;

    @FindBy(xpath = "//div[@class='zip-modal-buttons']//button[contains(text(),'Confirm')]") // Confirm button
    public WebElement confirmButton;

    @FindBy(xpath = "//div[@class='footer-copy']//p[2]")
    // Example footer element for content validation
    public WebElement warrantyPageFooterCopyright;

    @FindBy(id = "onetrust-close-btn-container")
    public WebElement cookieWindowCloseButton;


    public HyundaiOwnersPage(WebDriver driver) {
        super(driver); // Call BasePage constructor
        System.out.println("Hyundai Owners Page initialised");
    }

    public boolean isCookiesWindowDisplayed() {
        return isElementDisplayed(acceptAllCookiesButton);
    }

    public void acceptCookies() {
        try {
            // Wait for the button to be clickable

            waitForElementClickability(acceptAllCookiesButton);
            acceptAllCookiesButton.click();
            System.out.println("Hyundai Owners Page: Accepted cookies.");

            // Wait for the banner to become invisible after clicking
            waitForInvisibility(acceptAllCookiesButton);

        } catch (Exception e) {
            System.out.println("Hyundai Owners Page: Cookie banner 'Accept All' button not found or not clickable within timeout. Proceeding. (Error: " + e.getMessage() + ")");
        }
    }

    public void closeCookieWindow()
    {
        try {
            waitForElementClickability(cookieWindowCloseButton);
            cookieWindowCloseButton.click();
            System.out.println("Cookie Window Closed on Warranty Page");

            waitForInvisibility(cookieWindowCloseButton);
        } catch (Exception e) {
            System.out.println("Hyundai Warranty Page: Cookie Window not found: (Error: " + e.getMessage() + ")");
        }
    }

    public void clickOnWarrantyLink() {
        System.out.println("Hyundai Owners Page: Warranty' link clicking.");
        waitForElementClickability(warrantyLink);
        clickElement(warrantyLink); //
        System.out.println("Hyundai Owners Page: Clicked 'Warranty' link.");
    }

    public boolean isZipCodeTextBoxDisplayed() {
        return isElementDisplayed(zipcode_textbox);

    }

    public void enterValueInZipCodeTextbox(String zipCode) {
        System.out.println("Warranty Page: ZipCode Input given");
        typeText(zipcode_textbox, zipCode);
    }

    public void clickOnConfirmButton() {
        System.out.println("Clicking Confirm Button");
        confirmButton.click();
    }

    public boolean isWarrantyPageFooterDisplayed() {

        try {
            waitForVisibility(warrantyPageFooterCopyright);
            return isElementDisplayed(warrantyPageFooterCopyright);
        } catch (Exception e) {
            System.err.println("Hyundai Owners Page: Warranty page footer not found or not visible: " + e.getMessage());
            return false;
        }
    }

    public String getFooterCopyrightText() {
        System.out.println("Print Copyright Disclaimer");
        return getElementText(warrantyPageFooterCopyright);

    }

    // You can add more methods here for other elements on the Hyundai Owners page or Warranty page if needed
}