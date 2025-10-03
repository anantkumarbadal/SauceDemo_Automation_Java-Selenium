package com.sauceDemo.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

public class CheckoutOverviewPage extends BasePageFunctions {

    @FindBy(css = ".title")
    private WebElement checkoutOverviewHeader;

    @FindBy(className = "summary_subtotal_label")
    private WebElement itemTotalLabel;

    @FindBy(id = "finish")
    private WebElement finishButton;

    @FindBy(id = "cancel")
    private WebElement cancelButton;

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
        System.out.println("CheckoutOverviewPage initialized.");
    }

    public boolean isCheckoutOverviewHeaderDisplayed() {
        return isElementDisplayed(checkoutOverviewHeader);
    }

    public String getCheckoutOverviewHeaderText() {
        return getElementText(checkoutOverviewHeader);
    }

    public String getItemTotal() {
        return getElementText(itemTotalLabel);
    }

    public CheckoutCompletePage clickFinish() {
        clickElement(finishButton);
        System.out.println("Clicked finish button on checkout overview page.");
        return new CheckoutCompletePage(driver); // Navigates to CheckoutCompletePage
    }

    public ProductsPage clickCancel() {
        clickElement(cancelButton);
        System.out.println("Clicked cancel button on checkout overview page.");
        return new ProductsPage(driver); // Navigates back to ProductsPage
    }

    @FindBy(className = "inventory_item_name") // Locator for product names in overview
    private List<WebElement> listedItemNames;

    @FindBy(className = "summary_tax_label") // Locator for Tax
    private WebElement taxLabel;

    @FindBy(className = "summary_total_label") // Locator for Grand Total
    private WebElement grandTotalLabel;


    public List<String> getListedItemNames() {
        return listedItemNames.stream()
                .map(item -> getElementText(item).trim())
                .collect(Collectors.toList());
    }

    public String getTax() {
        return getElementText(taxLabel).trim();
    }

    public String getGrandTotal() {
        return getElementText(grandTotalLabel).trim();
    }



}