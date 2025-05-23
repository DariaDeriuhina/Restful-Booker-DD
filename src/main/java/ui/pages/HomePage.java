package ui.pages;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import lombok.experimental.Accessors;
import ui.pages.sections.BookingSection;
import ui.pages.sections.ContactSection;
import ui.pages.sections.LocationSection;
import ui.pages.sections.RoomsSection;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@Getter
@Accessors(fluent = true)
public class HomePage extends BasePage {
    private final SelenideElement pageTitle;
    private final BookingSection booking;
    private final ContactSection contact;
    private final LocationSection location;
    private final RoomsSection rooms;

    public HomePage() {
        pageTitle = $("h1");
        booking = new BookingSection();
        contact = new ContactSection();
        location = new LocationSection();
        rooms = new RoomsSection();
    }

    public HomePage openHomePage() {
        openPage("/");
        return this;
    }

    public BookingSection goToBookingViaButton(String buttonText) {
        clickButtonByText(buttonText);
        return booking();
    }
}
