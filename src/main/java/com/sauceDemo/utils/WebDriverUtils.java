package com.sauceDemo.utils;

/*
WebDriverUtils's Responsibility: Its role is to provide general,
cross-cutting utility methods that operate on the WebDriver instance itself or the browser.
These are actions that are not necessarily tied to a specific page or element,
but rather to the overall browser state or advanced interactions. Examples:

Executing JavaScript (scrolling, DOM manipulation).
Switching between windows or frames.
Handling alerts.
Taking full-page screenshots (if not handled by listeners).
More advanced explicit waits not directly associated with a specific element method.

WebDriverUtils is designed for composition (or direct usage of static methods).
You don't "inherit" general browser actions. Instead, you use them from anywhere
in your test code or Page Objects when needed (e.g., WebDriverUtils.scrollToBottom(driver)).

When you see WebDriverUtils.switchToWindowByTitle(driver, "new tab"), you instantly know it's a general browser manipulation.
 */


import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set; // For window handles
import java.util.function.Function;

// This utility class provides reusable methods for common WebDriver operations
public class WebDriverUtils {



    // ------------------------   JavaScript Executor Methods ----------------------------------

    public static Object executeJavaScript(WebDriver driver, String script, Object... args) {
        if (driver instanceof JavascriptExecutor) {
            return ((JavascriptExecutor) driver).executeScript(script, args);
        } else {
            System.err.println("WebDriver does not support JavaScript execution.");
            return null;
        }
    }

    public static void scrollToTop(WebDriver driver) {
        executeJavaScript(driver, "window.scrollTo(0, 0);");
    }

    public static void scrollToBottom(WebDriver driver) {
        executeJavaScript(driver, "window.scrollTo(0, document.body.scrollHeight);");
    }

    public static void scrollToElement(WebDriver driver, WebElement element) {
        executeJavaScript(driver, "arguments[0].scrollIntoView(true);", element);
        System.out.println("Scrolled to Element: " + element);
    }

    // ----------------------------- Frame Handling Methods -----------------------------------------

    /*
     index- The index of the frame to switch to (0-based).
     */
    public static void switchToFrameByIndex(WebDriver driver, int index) {
        driver.switchTo().frame(index);
    }

    /**
     * nameOrId- The name or ID of the frame to switch to.
     */
    public static void switchToFrameByNameOrId(WebDriver driver, String nameOrId) {
        driver.switchTo().frame(nameOrId);
    }

    public static void switchToDefaultContent(WebDriver driver) {
        driver.switchTo().defaultContent();
    }


    //---------------------------------Element actions----------------------------------------

    public static WebElement waitForElementClickable(WebDriver driver, WebElement element, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }


    // --------------------------------- Window Handle Methods ----------------------------------

    /**
     * Private static helper method to check if a new window handle has appeared.
     * @param initialHandles The set of window handles before the new window opened.
     * @param currentHandles The current set of window handles.
     * @return True if a new window handle is detected, false otherwise.
     */
    private static boolean isNewWindowDetected(Set<String> initialHandles, Set<String> currentHandles) {
        // Create a mutable copy of currentHandles to perform removeAll
        Set<String> newlyOpenedHandles = new HashSet<>(currentHandles);
        newlyOpenedHandles.removeAll(initialHandles);
        return !newlyOpenedHandles.isEmpty();
    }

    /**
     * Switches to the first new window/tab that appears after an action that opens a new window.
     * This method uses WebDriverWait with a custom ExpectedCondition for robustness.
     *
     * @param driver The WebDriver instance.
     * @param initialWindowHandles The set of window handles present *before* the action that opened the new window.
     * @param timeoutInSeconds The maximum time to wait for a new window to appear.
     * @return The window handle of the newly switched window, or null if no new window appears within the timeout.
     * The caller is responsible for switching back to the original window using switchToOriginalWindow().
     */
    public static String switchToFirstNewlyOpenedWindow(WebDriver driver, final Set<String> initialWindowHandles, int timeoutInSeconds) {
        System.out.println("DEBUG WU: Entered switchToFirstNewlyOpenedWindow. Initial handles count (from caller): " + initialWindowHandles.size());

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));

        try {
            // Wait until a new window is detected using the custom ExpectedCondition
            wait.until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driver) {
                    Set<String> currentHandles = driver.getWindowHandles();
                    System.out.println("DEBUG WU: Polling... Current handles: " + currentHandles.size());
                    return isNewWindowDetected(initialWindowHandles, currentHandles);
                }

                @Override
                public String toString() {
                    return "a new window to appear that was not in the initial set.";
                }
            });

            // After the wait, find the new window handle
            Set<String> allCurrentHandles = driver.getWindowHandles();
            Set<String> newlyFoundHandles = new HashSet<>(allCurrentHandles);
            newlyFoundHandles.removeAll(initialWindowHandles);

            if (!newlyFoundHandles.isEmpty()) {
                String newWindowHandle = newlyFoundHandles.iterator().next(); // Get the first new handle
                System.out.println("DEBUG WU: New window handle found: " + newWindowHandle);
                return newWindowHandle; // Return the handle
            } else {
                System.err.println("DEBUG WU: New window detected by wait, but no new handle found in subsequent check. This is unexpected.");
                return null;
            }

        } catch (Exception e) {
            System.err.println("DEBUG WU: Timeout or error waiting for new window: " + e.getMessage());
            return null;
        }
    }

    /**
     * Switches back to the original window/tab using its handle.
     * @param driver The WebDriver instance.
     * @param originalWindowHandle The handle of the window to switch back to.
     */


    public static void switchToOriginalWindow(WebDriver driver, String originalWindowHandle) {
        driver.switchTo().window(originalWindowHandle);
        System.out.println("DEBUG WU: Switched back to original window: " + originalWindowHandle);
    }

    //---------------------------------Wait Statements----------------------------------------

    public static void waitForPageLoad(WebDriver driver, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
        System.out.println("Page load complete.");
    }

    // You can add more utility methods here (e.g., for alerts, taking screenshots, etc.)




}
