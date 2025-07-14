package ui.elements;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

@Getter
public abstract class BaseElement {
    protected SelenideElement body;

    public void scrollToElementCenter(SelenideElement element) {
        Selenide.executeJavaScript(
                "arguments[0].scrollIntoView({block: 'center', inline: 'center'});", element);
    }
}
