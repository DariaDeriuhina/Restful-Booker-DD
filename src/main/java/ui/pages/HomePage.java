package ui.pages;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import lombok.experimental.Accessors;
import ui.components.sections.BookingSection;
import ui.components.sections.ContactSection;
import ui.components.sections.LocationSection;
import ui.components.sections.RoomsSection;

import static com.codeborne.selenide.Selenide.$;

@Getter
@Accessors(fluent = true)
public class HomePage extends BasePage {
    private final SelenideElement pageTitle;
    private final BookingSection booking;
    private final ContactSection contact;
    private final LocationSection location;
    private final RoomsSection rooms;
    private final SelenideElement bookingBtn;

    public HomePage() {
        pageTitle = $("h1");
        booking = new BookingSection();
        contact = new ContactSection();
        location = new LocationSection();
        rooms = new RoomsSection();
        bookingBtn = $("[href='#booking']");
    }

    public HomePage openHomePage() {
        openPage("/");
        return this;
    }

    public BookingSection goToBookingViaButton() {
        bookingBtn.click();
        return booking();
    }
}
