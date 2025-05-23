package ui.pages;

import com.codeborne.selenide.Selenide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ui.elements.BaseElement;

public abstract class BasePage extends BaseElement {
    protected static final Logger logger = LoggerFactory.getLogger(BasePage.class);

    protected void openPage(String url) {
        Selenide.open(url);
        logger.info("Page opened: {}", url);
    }
}
