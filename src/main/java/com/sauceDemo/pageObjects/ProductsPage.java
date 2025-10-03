package com.sauceDemo.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.By; // Added for dynamic item selection

public class ProductsPage extends BasePageFunctions {

    @FindBy(css = ".title")
    private WebElement productsHeader;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement burgerMenuButton;

    @FindBy(className = "shopping_cart_link")
    private WebElement shoppingCartLink;

    public ProductsPage(WebDriver driver) {
        super(driver);
        System.out.println("ProductsPage initialized.");
    }

    public boolean isProductsHeaderDisplayed() {
        return isElementDisplayed(productsHeader);
    }

    public String getProductsHeaderText() {
        return getElementText(productsHeader);
    }

    public ProductsPage openBurgerMenu() {
        clickElement(burgerMenuButton);
        System.out.println("Opened burger menu.");
        return this;
    }


    public ProductsPage addItemToCart(String itemName) {
        // Construct a dynamic locator for the add to cart button based on item name
        By addToCartButtonLocator = By.xpath("//div[normalize-space(text())='" + itemName + "']/ancestor::div[@class='inventory_item_label']/following-sibling::div[@class='pricebar']/button[normalize-space(text())='Add to cart']");

        /*
        // Convert the item name to the format used in the button ID for removing
        // The ID for remove buttons is similar: "remove-sauce-labs-backpack"
        String itemId = itemName.toLowerCase().replace(" ", "-");
        By removeButtonLocator = By.id("remove-" + itemId);
         */

        clickElement(driver.findElement(addToCartButtonLocator));
        System.out.println("Added '" + itemName + "' to cart.");
        return this;
    }

    public ProductsPage removeItemFromCart(String itemName) {
        String itemId = itemName.toLowerCase().replace(" ", "-");
        By removeButtonLocator = By.id("remove-" + itemId);

        clickElement(driver.findElement(removeButtonLocator));
        System.out.println("Removed '" + itemName + "' from cart.");
        return this; // Stay on the products page
    }

    public boolean isShoppingCartDisplayed()
    {
        return isElementDisplayed(shoppingCartLink);
    }

    public CartPage clickShoppingCart() {
        clickElement(shoppingCartLink);
        System.out.println("Navigated to shopping cart.");
        return new CartPage(driver); // Navigates to CartPage
    }

    @FindBy(className = "shopping_cart_badge") // Adjust locator if different
    private WebElement shoppingCartBadge;

    public int getShoppingCartBadgeCount() {
        if (isElementDisplayed(shoppingCartBadge)) { // Use BasePage method
            String badgeText = getElementText(shoppingCartBadge); // Use BasePage method
            try {
                return Integer.parseInt(badgeText.trim());
            } catch (NumberFormatException e) {
                System.err.println("Could not parse shopping cart badge text to integer: " + badgeText);
                return 0; // Return 0 or throw a custom exception
            }
        }
        return 0; // If badge is not displayed, assume 0 items
    }


}