package ui.pages.sections;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LocationSection extends BaseSection {

    public LocationSection() {
        super($("#location"));
    }

    public void verifyTitle(String title) {
        verifySectionTitle(title);
    }

    public void verifyMapLoaded() {
        $("img[src*='cartodb-basemaps']").shouldBe(visible);;
    }
}
