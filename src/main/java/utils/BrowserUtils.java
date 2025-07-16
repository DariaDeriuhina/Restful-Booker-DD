package utils;

import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.executeJavaScript;

public class BrowserUtils {

    public static void openNewTabAndReturn() {
        var driver = WebDriverRunner.getWebDriver();
        var originalWindow = driver.getWindowHandle();

        executeJavaScript("window.open('about:blank', '_blank');");

        var wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(d -> d.getWindowHandles().size() > 1);

        for (var handle : driver.getWindowHandles()) {
            if (!handle.equals(originalWindow)) {
                driver.switchTo().window(handle);
                break;
            }
        }

        driver.close();
        driver.switchTo().window(originalWindow);
    }
}