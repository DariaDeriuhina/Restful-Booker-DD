package utils.assertions;

import static com.codeborne.selenide.Selenide.executeJavaScript;
import static org.assertj.core.api.Assertions.assertThat;

public class UiAssertions {

    public static void assertAnchorHash(String expectedHash) {
        var actual = executeJavaScript("return window.location.hash;");
        assertThat(actual)
                .as("Expected URL hash to be " + expectedHash)
                .isEqualTo(expectedHash);
    }
}