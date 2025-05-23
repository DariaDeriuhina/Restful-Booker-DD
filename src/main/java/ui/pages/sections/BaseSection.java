package ui.pages.sections;

import com.codeborne.selenide.SelenideElement;
import ui.elements.BaseElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class BaseSection extends BaseElement {

    protected final SelenideElement section;

    protected BaseSection(SelenideElement section) {
        this.section = section;
    }

    protected void verifySectionTitle(String expectedText) {
        section.$("h2, h3").shouldBe(visible).shouldHave(text(expectedText));
    }
}
