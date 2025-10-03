package com.sauceDemo.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.stream.Collectors;

public class DashboardPage extends BasePageFunctions {

    @FindBy(id = "react-burger-menu-btn")
    private WebElement hamburgerMenuButton;

    @FindBy(id = "react-burger-cross-btn")
    private WebElement closeMenuButton;

    @FindBy(className = "title")
    private WebElement productsPageTitle;


    public DashboardPage(WebDriver driver) {
        super(driver); // Call constructor of BasePage
        System.out.println("Dashboard Page initialized.");
    }

    public boolean isHamburgerMenuButtonDisplayed()
    {
        return isElementDisplayed(hamburgerMenuButton);
    }

    public void openHamburgerMenu() {

        waitForVisibility(hamburgerMenuButton);
        clickElement(hamburgerMenuButton);
        System.out.println("Hamburger Menu Opened");
    }

    public void closeHamburgerMenu() {
        // Use clickElement from BasePage
        clickElement(closeMenuButton);
    }

    public List<String> getHamburgerMenuItems() {

        List<WebElement> items = driver.findElements(By.cssSelector(".bm-item-list a"));

        return items.stream()
                .map(item -> getElementText(item))
                .collect(Collectors.toList());
    }

    public String getProductsPageTitleText() {

        return getElementText(productsPageTitle);
    }

    @FindBy(xpath = "//a[@id='logout_sidebar_link']")
    private WebElement logoutLink;

    public boolean isLogoutButtonDisplayed()
    {
        waitForVisibility(logoutLink);
        return isElementDisplayed(logoutLink);
    }

    public LoginPage clickLogout() {
        waitForVisibility(logoutLink); // Ensure logout link is visible after menu opens
        clickElement(logoutLink);
        System.out.println("Clicked logout button.");

        return new LoginPage(driver); // Navigates to LoginPage
    }


}