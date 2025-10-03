package com.sauceDemo.bases;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class BaseTest {

    // ThreadLocal to manage WebDriver instances for parallel execution
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    // Getter for WebDriver instance specific to the current thread
    public WebDriver getDriver() {
        return driver.get();
    }

    @BeforeMethod
    @Parameters({"browser", "incognito", "headless"})
    public void setup(@Optional("chrome") String browser,
                      @Optional("false") boolean incognito,
                      @Optional("false") boolean headless,
                      ITestContext context) {

        WebDriver webDriver;
        StringBuilder browserModeInfo = new StringBuilder(browser); // To build string for Extent Report

        switch (browser.toLowerCase()) {

            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                boolean isSpecialChromeMode = false;
                if (incognito) {
                    chromeOptions.addArguments("--incognito");
                    browserModeInfo.append(" (Incognito)");
                    System.out.println("Chrome: Running in Incognito mode.");
                    isSpecialChromeMode = true;
                }
                if (headless) {
                    chromeOptions.addArguments("--headless=new"); // For newer Chrome versions
                    browserModeInfo.append(" (Headless)");
                    System.out.println("Chrome: Running in Headless mode.");
                    isSpecialChromeMode = true;
                }

                if (!isSpecialChromeMode) {
                    browserModeInfo.append(" (Standard)");
                    System.out.println("Chrome: Running in Standard mode.");
                }

                // Add other common headless arguments for robustness (only if headless)
                if (headless) {
                    chromeOptions.addArguments("--disable-gpu");
                    chromeOptions.addArguments("--window-size=1920,1080");
                    chromeOptions.addArguments("--no-sandbox"); // Bypass OS security model (Linux only)
                    chromeOptions.addArguments("--disable-dev-shm-usage"); // Overcome limited resource problems
                }
                webDriver = new ChromeDriver(chromeOptions);
                break;

            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                boolean isSpecialFirefoxMode = false;

                if (incognito) {
                    firefoxOptions.addArguments("-private"); // Firefox private mode
                    browserModeInfo.append(" (Private)");
                    System.out.println("Firefox: Running in Private mode.");
                    isSpecialFirefoxMode = true;
                }
                if (headless) {
                    firefoxOptions.addArguments("-headless");
                    browserModeInfo.append(" (Headless)");
                    System.out.println("Firefox: Running in Headless mode.");
                    isSpecialFirefoxMode = true;
                }

                if (!isSpecialFirefoxMode) {
                    browserModeInfo.append(" (Standard)");
                    System.out.println("firefox: Running in Standard mode.");
                }
                webDriver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                webDriver = new EdgeDriver();
                browserModeInfo.append(" (Standard)");
                System.out.println("Edge: Running in standard mode.");
                break;

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driver.set(webDriver); // Set WebDriver for the current thread

        // Store WebDriver and BrowserMode in ITestContext for TestListener
        context.setAttribute("WebDriver", webDriver);
        context.setAttribute("BrowserMode", browserModeInfo.toString());

        System.out.println("Browser setup: " + browser + " initialized. Mode: " + browserModeInfo.toString());
    }

    @AfterMethod
    public void teardown(ITestResult result) {
        if (getDriver() != null) {
            getDriver().quit(); // Quits the browser
            driver.remove(); // Removes the WebDriver instance from ThreadLocal
            System.out.println("Browser session closed.");
        }
    }

    /**
     * Takes a screenshot of the current browser state.
     * Returns the relative path to the screenshot (e.g., "screenshots/image.png") relative to the reports folder.
     */
    public static String takeScreenshot(WebDriver driver, String methodName) {
        if (driver == null || !(driver instanceof TakesScreenshot)) {
            System.err.println("Screenshot error: WebDriver is null or does not support screenshots.");
            return null;
        }

        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = methodName + "_" + timestamp + ".png";

        // Screenshots will be saved inside the "reports" directory, in a "screenshots" subfolder.
        String screenshotsDirPath = System.getProperty("user.dir") + File.separator + "reports" + File.separator + "screenshots";
        File screenshotsDir = new File(screenshotsDirPath);
        if (!screenshotsDir.exists()) {
            screenshotsDir.mkdirs(); // Ensure directory exists
        }

        File destination = new File(screenshotsDir, fileName);

        try {
            FileUtils.copyFile(source, destination);
            // Return path relative to the HTML report. If report is in /reports and screenshot in /reports/screenshots,
            // then the path within the report is simply "screenshots/filename.png"
            String relativePathForReport = "screenshots" + File.separator + fileName;
            System.out.println("Screenshot saved: " + destination.getName());
            return relativePathForReport.replace(File.separator, "/"); // Ensure forward slashes for HTML path
        } catch (IOException e) {
            System.err.println("Failed to save screenshot: " + destination.getName() + " - " + e.getMessage());
            return null;
        }
    }
}