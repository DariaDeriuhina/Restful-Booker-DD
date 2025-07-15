package ui.pages;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import ui.elements.BaseElement;
import ui.components.Header;
import utils.WaitUtils;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$$;

@Getter
@Slf4j
@Accessors(fluent = true)
public abstract class BasePage extends BaseElement {
    public Header header = new Header();

    protected BasePage() {
        WaitUtils.waitForPageToLoad();
    }

    @Step("Open page {}")
    protected void openPage(String url) {
        Selenide.open(url);
        WaitUtils.waitForPageToLoad();
        WaitUtils.waitForJQuery();
        log.info("Page opened: {}", url);
    }

    protected void clickButtonByText(String buttonText) {
        var button = $$("button").findBy(text(buttonText));
        scrollToElementCenter(button);
        button.shouldBe(visible).click();
    }
}
