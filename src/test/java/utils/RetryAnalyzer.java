package utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int attempt = 0;
    private static final int maxRetries = 2;

    @Override
    public boolean retry(ITestResult result) {
        return attempt++ < maxRetries;
    }
}