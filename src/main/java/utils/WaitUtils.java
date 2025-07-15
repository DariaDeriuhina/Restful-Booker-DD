package utils;

import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.executeJavaScript;

public class WaitUtils {

    public static void waitForPageToLoad() {
        var wait = new WebDriverWait(WebDriverRunner.getWebDriver(), Duration.ofSeconds(10));
        wait.until(driver -> {
            var readyState = executeJavaScript("return document.readyState");
            return "complete".equals(readyState);
        });
    }

    public static void waitForJQuery() {
        try {
            var wait = new WebDriverWait(WebDriverRunner.getWebDriver(), Duration.ofSeconds(5));
            wait.until(driver -> {
                Boolean jQueryDefined = executeJavaScript("return typeof jQuery !== 'undefined'");
                if (!jQueryDefined) return true;

                Long activeRequests = executeJavaScript("return jQuery.active");
                return activeRequests == 0;
            });
        } catch (Exception ignored) {
        }
    }
}
