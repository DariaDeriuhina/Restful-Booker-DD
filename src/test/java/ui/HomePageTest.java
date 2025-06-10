package ui;

import org.testng.annotations.Test;
import ui.components.AlertMessage;
import ui.pages.HomePage;
import ui.components.sections.RoomsSection;
import test_data.BookingTestData;
import test_data.ContactTestData;
import utils.ScrollUtils;
import utils.base.BaseTest;

import java.time.LocalDate;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static org.assertj.core.api.Assertions.assertThat;

public class HomePageTest extends BaseTest {

    @Test(description = "BUG: Form submits with invalid Check In/Out dates",
            dataProvider = "invalidBookingDates", dataProviderClass = BookingTestData.class)
    public void invalidBookingDatesShouldDisableBooking(String checkIn, String checkOut) {
        var homePage = new HomePage();
        var bookingSection = homePage.header().clickBooking();

        bookingSection
                .applyCheckInDate(checkIn)
                .applyCheckOutDate(checkOut)
                .clickOnCheckAvailability();

        var roomSection = new RoomsSection();
        var firstRoom = roomSection.getFirstRoomCard();

        firstRoom.bookNowBtn()
                .shouldBe(visible)
                .shouldHave(attribute("disabled"));
    }

    @Test(description = "Verify All Section Titles")
    public void sectionTitlesTest() {
        var homePage = new HomePage();
        assertThat(homePage.pageTitle().getText()).isEqualTo("Welcome to Shady Meadows B&B");
        homePage.booking().verifySectionTitle("Check Availability & Book Your Stay");
        homePage.rooms().verifySectionTitle("Our Rooms");
        homePage.contact().verifySectionTitle("Send Us a Message");
        homePage.location().verifySectionTitle("Our Location");
    }

    @Test(description = "Verify scroll to sections via header")
    public void scrollToSectionsByHeaderTest() {
        var homePage = new HomePage();
        homePage.header().clickRooms();
        ScrollUtils.assertAnchorHash("#rooms");

        homePage.header().clickBooking();
        ScrollUtils.assertAnchorHash("#booking");

        homePage.header().clickLocation();
        ScrollUtils.assertAnchorHash("#location");

        homePage.header().clickContact();
        ScrollUtils.assertAnchorHash("#contact");
    }

    @Test(description = "Check Availability button leads to Rooms section validation",
            dataProvider = "validBookingDates", dataProviderClass = BookingTestData.class)
    public void checkAvailabilityBtnLeadToRoomsTest(LocalDate checkIn, LocalDate checkOut) {
        var homePage = new HomePage();
        var bookingSection = homePage.header.clickBooking();

        var roomSection = bookingSection.applyCheckInDate(checkIn)
                .applyCheckOutDate(checkOut)
                .clickOnCheckAvailability();

        var firstRoom = roomSection.getFirstRoomCard();
        firstRoom.bookNowBtn().shouldBe(clickable);
    }

    @Test(description = "Contact form validation",
            dataProvider = "contactFormData", dataProviderClass = ContactTestData.class)
    public void contactFormTest(String name, String email, String phone, String subject, String message, List<String> expectedWarnings) {
        var expectedConfirmationMessage = "Thanks for getting in touch %1$s! We'll get back to you about %2$s as soon as possible.".formatted(name, subject);
        var contactSection = new HomePage().contact()
                .fillForm(name, email, phone, subject, message)
                .submit();

        var alert = new AlertMessage();

        if(expectedWarnings.isEmpty()) {
            alert.getBody().shouldNotBe(visible);
            contactSection.submitBtn().should(disappear);
            assertThat(contactSection.getFullConfirmationMessage()).isEqualTo(expectedConfirmationMessage);
        } else {
            alert.getBody().shouldBe(visible);
            alert.shouldHaveMessages(expectedWarnings);
        }
    }
}

