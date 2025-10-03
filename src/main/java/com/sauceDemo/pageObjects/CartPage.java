package com.sauceDemo.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

public class CartPage extends BasePageFunctions {

    @FindBy(css = ".title")
    private WebElement cartHeader;

    @FindBy(className = "inventory_item_name")
    private WebElement itemNameInCart; // Assuming for simplicity we check the first item

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingButton;

    public CartPage(WebDriver driver) {
        super(driver);
        System.out.println("CartPage initialized.");
    }

    public boolean isCartHeaderDisplayed() {
        return isElementDisplayed(cartHeader);
    }

    public String getCartHeaderText() {
        return getElementText(cartHeader);
    }

    public String getItemNameInCart() {
        return getElementText(itemNameInCart);
    }

    public CheckoutInformationPage clickCheckout() {
        clickElement(checkoutButton);
        System.out.println("Clicked checkout button.");
        return new CheckoutInformationPage(driver); // Navigates to CheckoutInformationPage
    }

    public ProductsPage clickContinueShopping() {
        clickElement(continueShoppingButton);
        System.out.println("Clicked continue shopping button.");
        return new ProductsPage(driver); // Navigates to ProductsPage
    }

    @FindBy(className = "inventory_item_name") // Locator for all item names in the cart
    private List<WebElement> cartItemNames;

    public List<String> getCartItemNames() {
        return cartItemNames.stream()
                .map(item -> getElementText(item).trim()) // Use BasePage method to get text
                .collect(Collectors.toList());
    }

}