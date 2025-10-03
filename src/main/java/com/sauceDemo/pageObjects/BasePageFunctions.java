package com.sauceDemo.pageObjects;

import com.sauceDemo.utils.ConfigLoader; // Corrected import

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePageFunctions {


    /*
    BasePage's Responsibility: Its primary role is to provide common
    functionality for Page Object classes. This includes things like:

    Initializing the WebDriver instance for Page Objects.

    Basic, common element interactions (click, type, isDisplayed) that are
    typically performed on elements within the context of a page.

    Possibly methods to get the current page URL or title.

    It acts as the base class that all your specific Page Objects will
    extend (LoginPage extends BasePage).

    BasePage is designed for inheritance. Your Page Objects inherit methods and
    properties from BasePage. This makes sense because a LoginPage is a BasePage with
    added specific elements and methods.

    When you see loginPage.enterUsername("user"), you instantly know it's a page-specific action.
     */

    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePageFunctions(WebDriver driver) {
        this.driver = driver;
        int timeout = ConfigLoader.getIntProperty("default.explicit.wait.seconds", 15);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        PageFactory.initElements(driver, this); // Initializes WebElements annotated with @FindBy
        System.out.println("BasePageFunctions initialized for: " + (driver != null ? driver.getCurrentUrl() : "No URL (driver null)"));
    }


    protected void clickElement(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
            System.out.println("Clicked on element: " + element.toString());
        } catch (Exception e) {
            String errorMsg = "Failed to click element: " + element.toString();
            System.err.println(errorMsg);
            throw new RuntimeException(errorMsg, e);
        }
    }

    // Add useful utility methods
    protected void waitForElementVisibility(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            int timeout = ConfigLoader.getIntProperty("default.explicit.wait.seconds", 15);
            throw new RuntimeException("Element not visible after " + timeout + " seconds: " + element, e);
        }
    }

    protected void typeText(WebElement element, String text) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.clear();
            element.sendKeys(text);
        } catch (Exception e) {
            throw new RuntimeException("Failed to type text into element: " + element, e);
        }
    }

    //************************Basic Element Validation*************************//

    protected WebElement waitForVisibility(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected WebElement waitForElementClickability(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected Boolean waitForInvisibility(WebElement element) {
        return wait.until(ExpectedConditions.invisibilityOf(element));
    }

    //Checks if element is visible
    protected boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException | org.openqa.selenium.StaleElementReferenceException e) {
            return false;
        }
    }

    //Checks if element is interactive
    protected boolean isElementEnabled(WebElement element) {
        try {
            return element.isEnabled();
        } catch (org.openqa.selenium.NoSuchElementException | org.openqa.selenium.StaleElementReferenceException e) {
            return false;
        }
    }

    //For checkboxes/radio buttons -Element is selected

    protected boolean isElementSelected(WebElement element) {
        try {
            return element.isSelected();
        } catch (org.openqa.selenium.NoSuchElementException | org.openqa.selenium.StaleElementReferenceException e) {
            return false;
        }
    }


    protected String getElementText(WebElement element) {
        try {
            return wait.until(ExpectedConditions.visibilityOf(element)).getText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get text from element: " + element, e);
        }
    }

    protected String getCurrentUrl(WebElement currentUrl) {
        return driver.getCurrentUrl();
    }


    // Add more common methods here as needed, e.g., for dropdowns, alerts, etc.
}