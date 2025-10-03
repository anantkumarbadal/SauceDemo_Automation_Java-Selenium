# SauceDemo_Automation_Java-Selenium
Automating the https://www.saucedemo.com/ portal with Java Selenium Automation Framework 

SauceDemo E-commerce Test Automation Framework
A robust, modular, and scalable test automation framework built using Selenium WebDriver, Java, and TestNG, following Page Object Model (POM) and industry-standard design patterns.
"This is a cross-browser, end-to-end (E2E) test automation framework designed to validate critical user journeys on the SauceDemo E-commerce platform."

Implemented Framework Features
Page Object Model (POM): Implemented for separation of UI elements and test logic.

Base Components:

BaseTest: Manages thread-safe WebDriver setup (ThreadLocal) and teardown.

BasePage: Contains reusable, common element interaction methods.

ExtentReports & TestNG: Configured for test execution management and detailed HTML reporting.

TestListener: Automatically handles logging test steps and capturing screenshots on failures.

Universal Utilities (WebDriverUtils):

Robust Window Handling: Implemented helpers (switchToFirstNewlyOpenedWindow / waitForNewWindowAndSwitch) for reliably switching to new tabs/windows.

Universal Wait Helpers: Includes methods for explicit element visibility (waitForElementToBeVisible) and full page loading (waitForPageLoad via JavaScript readyState).

JavaScript Execution: Dedicated helper (executeJavaScript) for safely running client-side scripts (e.g., scrolling).

Completed Test Cases: Functionality verified up through TC012, successfully demonstrating the robust new window handling on an external portal (Hyundai).


