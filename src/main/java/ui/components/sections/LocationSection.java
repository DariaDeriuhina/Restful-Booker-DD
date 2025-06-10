package ui.components.sections;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LocationSection extends BaseSection {

    public LocationSection() {
        super($("#location"));
    }

    public void verifyMapLoaded() {
        $("img[src*='cartodb-basemaps']").shouldBe(visible);
    }
}
