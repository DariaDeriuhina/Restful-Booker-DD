package utils;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;

import static com.codeborne.selenide.Selenide.executeJavaScript;

public class BrowserUtils {

    public static void openNewTabAndReturn() {
        var driver = WebDriverRunner.getWebDriver();
        var originalWindow = driver.getWindowHandle();

        executeJavaScript("window.open('about:blank', '_blank');");
        var allWindows = driver.getWindowHandles();

        for (var handle : allWindows) {
            if (!handle.equals(originalWindow)) {
                driver.switchTo().window(handle);
                break;
            }
        }

        Selenide.sleep(1000);

        driver.close();
        driver.switchTo().window(originalWindow);
    }
}
