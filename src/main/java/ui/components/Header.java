package ui.components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import ui.components.sections.BookingSection;
import ui.components.sections.ContactSection;
import ui.components.sections.LocationSection;
import ui.components.sections.RoomsSection;
import ui.elements.BaseElement;

import static com.codeborne.selenide.Selenide.$;

public class Header extends BaseElement {
    private final SelenideElement roomsLink;
    private final SelenideElement bookingLink;
    private final SelenideElement contactLink;
    private final SelenideElement locationLink;

    public Header() {
        roomsLink = $("[href='/#rooms']");
        bookingLink = $("[href='/#booking']");
        contactLink = $("[href$='#contact']");
        locationLink = $("[href$='#location']");
    }

    @Step("Click on 'Rooms' link in header")
    public void clickRooms() {
        roomsLink.click();
        new RoomsSection();
    }
    @Step("Click on 'Booking' link in header")
    public BookingSection clickBooking() {
        bookingLink.click();
        return new BookingSection();
    }

    @Step("Click on 'Contact' link in header")
    public void clickContact() {
        contactLink.click();
        new ContactSection();
    }

    @Step("Click on 'Location' link in header")
    public void clickLocation() {
        locationLink.click();
        new LocationSection();
    }
}
