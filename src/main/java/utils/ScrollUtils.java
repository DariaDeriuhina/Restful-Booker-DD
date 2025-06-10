package utils;

import static com.codeborne.selenide.Selenide.executeJavaScript;
import static org.assertj.core.api.Assertions.assertThat;

public class ScrollUtils {

    public static void assertAnchorHash(String expectedHash) {
        String actual = executeJavaScript("return window.location.hash;");
        assertThat(actual)
                .as("Expected URL hash to be " + expectedHash)
                .isEqualTo(expectedHash);
    }
}