package utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.concurrent.atomic.AtomicInteger;

public class RetryAnalyzer implements IRetryAnalyzer {
    private final AtomicInteger attempt = new AtomicInteger(0);
    private final int maxRetries = Integer.parseInt(System.getProperty("retry.count", "2"));

    @Override
    public boolean retry(ITestResult result) {
        return attempt.incrementAndGet() <= maxRetries;
    }
}