package com.sauceDemo.testngUtils;

/*
TestNG's IRetryAnalyzer (For Automatic Retries of Transient Failures)
This is a more advanced technique for automatically retrying tests during the same
execution if they fail due to transient issues (e.g., network glitch, element not loaded
immediately).
How it works: You implement the IRetryAnalyzer interface and apply it to
your @Test methods. If a test fails, TestNG calls your retry logic to decide if
it should be re-executed.
Benefit: Catches flakiness without manual intervention.
 */
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = 1; // Retry a test up to 2 times (total 3 attempts including the original)

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            System.out.println("Retrying test " + result.getName() + " for the " + (retryCount + 1) + " time.");
            retryCount++;
            return true; // Retry the test
        }
        return false; // Do not retry
    }
}

/*
TestNG's Internal State:

When a test method fails (Assert.fail()), TestNG consults the IRetryAnalyzer.
If retry() returns true (meaning, "yes, try again"),
TestNG does not immediately mark that specific attempt as a definitive FAIL.
Instead, it internally treats it as a "skipped for retry" or "failure that will be retried" state.
It then re-runs the test.
TestListener Interaction:

When TestNG internally marks an attempt as "skipped for retry," it triggers the
onTestSkipped() method in your TestListener for that specific attempt.
This is why you see "skipped" in the report.
The onTestFailure() method is only invoked if the test fails even after
all retry attempts defined by MAX_RETRY_COUNT are exhausted.
Your RetryAnalyzer Logic:

Your MAX_RETRY_COUNT is set to 2. This means:
Attempt 1 (Original Run): retryAttemptCount is 0. It fails, retry() returns true.
 TestNG triggers onTestSkipped().
Attempt 2 (1st Retry): retryAttemptCount is 1. It fails, retry() returns true.
 TestNG triggers onTestSkipped().
Attempt 3 (2nd Retry): retryAttemptCount is 2. The if (retryAttemptCount < 2) condition is false,
 so the Assert.fail() is skipped, and the test continues to pass its actual assertions. Since this is the final attempt and it passed, TestNG marks the overall test as PASS. onTestSuccess() is then triggered.
 */
