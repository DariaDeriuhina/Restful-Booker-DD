package ui.pages.sections;

import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class RoomsSection extends BaseSection {
    private final ElementsCollection roomCards;

    public RoomsSection() {
        super($("#rooms"));
        roomCards = $$(".room-card");
    }

    public void verifyTitle(String title) {
        verifySectionTitle(title);
    }

    public void verifyRoomCardsVisible() {
        roomCards.shouldBe(sizeGreaterThan(0));
    }

    public void bookFirstRoom() {
        var firstBookBtn = roomCards.first().$("a.btn");
        firstBookBtn.click();
    }
}
