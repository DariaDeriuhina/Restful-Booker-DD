package utils;

import lombok.extern.slf4j.Slf4j;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class RetryAnalyzer implements IRetryAnalyzer {
    private final AtomicInteger attempt = new AtomicInteger(0);
    private final int maxRetries = Integer.parseInt(System.getProperty("retry.count", "2"));

    @Override
    public boolean retry(ITestResult result) {
        int currentAttempt = attempt.incrementAndGet();
        log.warn("Retrying test '{}' (attempt {}/{}) after failure",
                result.getName(), currentAttempt, maxRetries);
        return currentAttempt <= maxRetries;
    }
}