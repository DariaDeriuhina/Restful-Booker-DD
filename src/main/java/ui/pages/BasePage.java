package ui.pages;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ui.elements.BaseElement;
import ui.components.Header;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$$;

@Getter
@Accessors(fluent = true)
public abstract class BasePage extends BaseElement {
    protected static final Logger logger = LoggerFactory.getLogger(BasePage.class);
    public Header header = new Header();

    @Step("Open page {}")
    protected void openPage(String url) {
        Selenide.open(url);
        logger.info("Page opened: {}", url);
    }

    protected void clickButtonByText(String buttonText) {
        var button = $$("button").findBy(text(buttonText));
        scrollToElementCenter(button);
        button.shouldBe(visible).shouldBe(enabled).click();
    }
}
