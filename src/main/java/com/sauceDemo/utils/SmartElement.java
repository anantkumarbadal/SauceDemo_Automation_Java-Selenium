package com.sauceDemo.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;

public class SmartElement {
    private static final Logger logger = LogManager.getLogger(SmartElement.class);
    private final WebDriver driver;
    private final By originalLocator;
    private final List<By> alternativeLocators;
    private WebElement element;
    private final WebDriverWait wait;
    private static final int DEFAULT_TIMEOUT = 10;
    private long startTime;

    public SmartElement(WebDriver driver, By originalLocator, By... alternativeLocators) {
        this.driver = driver;
        this.originalLocator = originalLocator;
        this.alternativeLocators = Arrays.asList(alternativeLocators);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        this.startTime = System.currentTimeMillis();
    }

    private WebElement findElement() {
        List<String> triedLocators = new ArrayList<>();
        Exception lastException = null;

        // Try original locator first
        try {
            element = wait.until(ExpectedConditions.presenceOfElementLocated(originalLocator));
            logger.debug("Element found with original locator: {}", originalLocator);
            return element;
        } catch (Exception e) {
            triedLocators.add(originalLocator.toString());
            lastException = e;
            logger.warn("Failed to find element with original locator: {}", originalLocator);
        }

        // Try alternative locators
        for (By alternativeLocator : alternativeLocators) {
            try {
                element = wait.until(ExpectedConditions.presenceOfElementLocated(alternativeLocator));
                logger.info("Element found with alternative locator: {}", alternativeLocator);
                return element;
            } catch (Exception e) {
                triedLocators.add(alternativeLocator.toString());
                lastException = e;
                logger.warn("Failed to find element with alternative locator: {}", alternativeLocator);
            }
        }

        // If all locators fail, try dynamic locator strategies
        try {
            element = findElementDynamically();
            if (element != null) {
                return element;
            }
        } catch (Exception e) {
            lastException = e;
            logger.error("Dynamic element location failed", e);
        }

        throw new NoSuchElementException("Element not found with any locators. Tried: " + String.join(", ", triedLocators), lastException);
    }

    private WebElement findElementDynamically() {
        // Try finding by similar attributes or nearby text
        String xpath = generateDynamicXPath();
        if (xpath != null) {
            try {
                element = driver.findElement(By.xpath(xpath));
                logger.info("Element found with dynamic XPath: {}", xpath);
                return element;
            } catch (Exception e) {
                logger.warn("Dynamic XPath location failed: {}", xpath);
            }
        }
        return null;
    }

    private String generateDynamicXPath() {
        if (originalLocator.toString().contains("id=")) {
            String id = originalLocator.toString().split("id=")[1];
            return String.format("//*[contains(@id,'%s') or contains(@name,'%s')]", id, id);
        }
        // Add more dynamic strategies as needed
        return null;
    }

    public void click() {
        recordStartTime();
        try {
            WebElement element = findElement();
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
            logExecutionTime("click");
        } catch (Exception e) {
            logger.error("Failed to click element", e);
            throw e;
        }
    }

    public void sendKeys(String text) {
        recordStartTime();
        try {
            WebElement element = findElement();
            element.clear();
            element.sendKeys(text);
            logExecutionTime("sendKeys");
        } catch (Exception e) {
            logger.error("Failed to send keys to element", e);
            throw e;
        }
    }

    public String getText() {
        recordStartTime();
        try {
            WebElement element = findElement();
            String text = element.getText();
            logExecutionTime("getText");
            return text;
        } catch (Exception e) {
            logger.error("Failed to get text from element", e);
            throw e;
        }
    }

    public boolean isDisplayed() {
        recordStartTime();
        try {
            WebElement element = findElement();
            boolean displayed = element.isDisplayed();
            logExecutionTime("isDisplayed");
            return displayed;
        } catch (Exception e) {
            logger.debug("Element is not displayed", e);
            return false;
        }
    }

    private void recordStartTime() {
        this.startTime = System.currentTimeMillis();
    }

    private void logExecutionTime(String action) {
        long executionTime = System.currentTimeMillis() - startTime;
        logger.info("{} operation took {} ms", action, executionTime);
    }
}
