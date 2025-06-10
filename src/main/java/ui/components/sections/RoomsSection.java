package ui.components.sections;

import com.codeborne.selenide.ElementsCollection;
import ui.components.RoomCard;

import java.util.List;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class RoomsSection extends BaseSection {
    private final ElementsCollection roomCards;

    public RoomsSection() {
        super($("#rooms"));
        roomCards = $$(".room-card");
    }

    public void verifyRoomCardsVisible() {
        roomCards.shouldBe(sizeGreaterThan(0));
    }

    public RoomCard getFirstRoomCard() {
        return new RoomCard(roomCards.first());
    }

    public List<RoomCard> getRoomCards() {
        return roomCards.stream()
                .map(RoomCard::new)
                .toList();
    }
}
