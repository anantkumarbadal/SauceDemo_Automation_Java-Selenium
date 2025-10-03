package com.sauceDemo.testsPractice;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

public class HelperUtils {

    public static String switchToFirstNewlyOpenedWindow(WebDriver driver, final Set<String> initialWindowHandles, int timeoutInSeconds) {
        System.out.println("DEBUG Helper: Entered switchToFirstNewlyOpenedWindow. Initial handles count: " + initialWindowHandles.size());

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));

        try {
            // Wait until a new window is detected using the custom ExpectedCondition
            wait.until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driver) {
                    Set<String> currentHandles = driver.getWindowHandles();
                    System.out.println("DEBUG Helper: Polling... Current handles: " + currentHandles.size());
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
                String newWindowHandle = newlyFoundHandles.iterator().next();
                System.out.println("DEBUG Helper: New window handle found: " + newWindowHandle);
                return newWindowHandle;
            } else {
                System.err.println("DEBUG Helper: New window detected by wait, but no new handle found in subsequent check. This is unexpected.");
                return null;
            }

        } catch (Exception e) {
            System.err.println("DEBUG Helper: Timeout or error waiting for new window: " + e.getMessage());
            return null;
        }
    }

    public static void switchToOriginalWindow(WebDriver driver, String originalWindowHandle) {
        driver.switchTo().window(originalWindowHandle);
        System.out.println("DEBUG Helper: Switched back to original window: " + originalWindowHandle);
    }

    public  static boolean isNewWindowDetected(Set<String> initialHandles, Set<String> currentHandles) {
        Set<String> newlyOpenedHandles = new HashSet<>(currentHandles);
        newlyOpenedHandles.removeAll(initialHandles);
        return !newlyOpenedHandles.isEmpty();
    }

    public static Object executeJavaScript(WebDriver driver, String script, Object... args) {
        if (driver instanceof JavascriptExecutor) {
            return ((JavascriptExecutor) driver).executeScript(script, args);
        } else {
            System.err.println("WebDriver does not support JavaScript execution.");
            return null;
        }
    }

}
