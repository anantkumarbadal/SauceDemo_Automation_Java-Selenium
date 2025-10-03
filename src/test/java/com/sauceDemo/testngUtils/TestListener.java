package com.sauceDemo.testngUtils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.sauceDemo.bases.BaseTest; // Corrected import
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestListener implements ITestListener {

    private ExtentReports extent;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    public static ExtentTest getExtentTest() { // New public static getter
        return extentTest.get();
    }
// And then in the tests, use TestListener.getExtentTest().info() etc.


    @Override
    public void onStart(ITestContext context) {
        String timestamp = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss").format(new Date());
        String reportFileName = "Automation_Report_" + timestamp + ".html";
        String reportsDirPath = System.getProperty("user.dir") + File.separator + "reports";
        File reportsDir = new File(reportsDirPath);
        if (!reportsDir.exists()) {
            reportsDir.mkdirs();
        }
        String reportPath = reportsDir.getAbsolutePath() + File.separator + reportFileName;

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setDocumentTitle("Automation Report");
        sparkReporter.config().setReportName("Test Results");
        sparkReporter.config().setTheme(Theme.STANDARD);

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("QA", "Anant Kumar");
        extent.setSystemInfo("Env", "Test");
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("Selenium Version", "4.15.0");
        extent.setSystemInfo("Framework", "Java+Selenium Automation Framework");
        extent.setSystemInfo("Application URL", "https://www.saucedemo.com/");

        System.out.println("Extent Report initialized at: " + reportPath);
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
            System.out.println("Extent Report flushed.");
        }
    }

    @Override
    public void onTestStart(ITestResult result) {

        String methodName = result.getMethod().getMethodName();
        Object[] params = result.getParameters(); // Get parameters passed by DataProvider (if any)
        String testDescription = result.getMethod().getDescription(); // Get description from @Test(description="...")

        String testName = methodName; // Default test name will be just the method name

        if (params != null && params.length > 0) {

            String username = (params.length > 0 && params[0] != null) ? params[0].toString() : "N/A_User";
            String expectedResult = (params.length > 2 && params[2] != null) ? params[2].toString() : "N/A_Expected";
            testName = methodName + " - User: " + username + " (Expected: " + expectedResult + ")";

        }

        ExtentTest test = extent.createTest(testName, testDescription); // Pass testDescription here as well
        extentTest.set(test); // Store the test in ThreadLocal

        String browserMode = (String) result.getTestContext().getAttribute("BrowserMode");
        if (browserMode != null) {
            test.assignCategory(browserMode); // Assign as a category for filtering
            test.info("Test executed in: " + browserMode); // Log this info for the test
        } else {
            test.info("Test executed in: Unknown Browser Mode (Error retrieving from context)");
            System.err.println("Error: BrowserMode attribute was null in ITestContext for test: " + result.getMethod().getMethodName());
        }

        System.out.println("Test Started: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.get().log(Status.PASS, MarkupHelper.createLabel(result.getMethod().getMethodName() + " PASSED", ExtentColor.GREEN));
        System.out.println("Test Passed: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        extentTest.get().log(Status.FAIL, MarkupHelper.createLabel(testName + " FAILED", ExtentColor.RED));
        extentTest.get().fail(result.getThrowable());

        WebDriver driver = (WebDriver) result.getTestContext().getAttribute("WebDriver");
        if (driver != null) {
            String screenshotRelativePath = BaseTest.takeScreenshot(driver, testName); // Path relative to "reports" folder
            if (screenshotRelativePath != null) {
                try {
                    extentTest.get().fail("Screenshot:",
                            com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromPath(screenshotRelativePath).build());
                    System.out.println("Screenshot attached to report for: " + testName);
                } catch (Exception e) {
                    System.err.println("Failed to attach screenshot to report for " + testName + ": " + e.getMessage());
                }
            }
        } else {
            System.err.println("No WebDriver instance found for " + testName + ". Cannot take screenshot for failure.");
        }
        System.out.println("Test Failed: " + testName);
    }

    @Override
    public void onTestSkipped(ITestResult result) {

        extentTest.get().log(Status.SKIP, MarkupHelper.createLabel(result.getMethod().getMethodName() + " SKIPPED", ExtentColor.ORANGE));
        extentTest.get().skip(result.getThrowable());
        System.out.println("Test Skipped: " + result.getMethod().getMethodName());

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }
}

/*
testng.xml Execution (Works Fine):

When you run your tests via testng.xml, TestNG's full lifecycle kicks in.
The <listeners> tag ensures your TestListener (which implements ISuiteListener
and ITestListener) is fully registered.
Crucially, onStart(ISuite suite) is called once before the entire suite starts.
This is where your private static ExtentReports extent; object is correctly initialized (e.g.,
extent = new ExtentReports();).
Then, for each test, onTestStart(ITestResult result) is called, and since extent is now
initialized, extent.createTest() works perfectly, and extentTest.set(test) successfully
stores the ExtentTest object.
Finally, your @Test method uses TestListener.getExtentTest().info(), which retrieves a
valid ExtentTest object, preventing the NPE.
 */