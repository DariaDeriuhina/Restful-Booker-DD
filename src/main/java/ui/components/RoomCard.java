package ui.components;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import lombok.experimental.Accessors;
import ui.elements.BaseElement;
import ui.pages.BookingPage;

@Getter
@Accessors(fluent = true)
public class RoomCard extends BaseElement {
   private final SelenideElement root;
   private final SelenideElement roomName;
   private final SelenideElement roomPrice;
   private final SelenideElement bookNowBtn;

   public RoomCard(SelenideElement root) {
       this.root = root;
       roomName = root.$(".room-name");
       roomPrice = root.$(".room-price");
       bookNowBtn = root.$("a.btn");
   }

    public String getRoomName() {
        return roomName.getText();
    }

    public String getPrice() {
        return roomPrice.getText();
    }

    public BookingPage clickBookNow() {
        scrollToElementCenter(bookNowBtn);
        bookNowBtn.click();
        return new BookingPage();
    }
}
