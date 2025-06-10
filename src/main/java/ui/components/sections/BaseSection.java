package ui.components.sections;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import ui.pages.BasePage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

@Getter
public class BaseSection extends BasePage {

    protected final SelenideElement section;

    protected BaseSection(SelenideElement section) {
        this.section = section;
    }

    public void verifySectionTitle(String expectedText) {
        section.$("h2, h3").shouldBe(visible).shouldHave(text(expectedText));
    }
}
