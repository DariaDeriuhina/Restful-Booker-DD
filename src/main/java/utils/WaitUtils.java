package utils;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.Wait;
import static com.codeborne.selenide.Selenide.executeJavaScript;

public class WaitUtils {

    private static final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration JQUERY_TIMEOUT = Duration.ofSeconds(5);

    public static void waitForPageToLoad() {
        Wait().withTimeout(PAGE_LOAD_TIMEOUT).until(driver ->
                "complete".equals(executeJavaScript("return document.readyState"))
        );
    }

    public static void waitForJQuery() {
        try {
            Wait().withTimeout(JQUERY_TIMEOUT).until(driver -> {
                var jQueryDefined = executeJavaScript("return typeof jQuery !== 'undefined'");
                if (Boolean.FALSE.equals(jQueryDefined)) return true;
                Long active = executeJavaScript("return jQuery.active");
                return active == 0;
            });
        } catch (Exception e) {
            System.err.println("Warning: jQuery wait failed: " + e.getMessage());
        }
    }
}