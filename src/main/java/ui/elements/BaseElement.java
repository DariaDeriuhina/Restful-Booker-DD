package ui.elements;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;

@Getter
public class BaseElement {

    protected SelenideElement body;

    protected void clickButtonByText(String buttonText) {
        $$("button").findBy(text(buttonText)).shouldBe(visible).click();
    }
}
