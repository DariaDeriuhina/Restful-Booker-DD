package utils;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;

import java.util.ArrayList;

public class BrowserUtils {

    public static void openNewTabAndReturn() {
        var driver = WebDriverRunner.getWebDriver();
        var originalWindow = driver.getWindowHandle();

        Selenide.executeJavaScript("window.open();");

        var tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(tabs.size() - 1));

        Selenide.closeWindow();

        driver.switchTo().window(originalWindow);
    }
}