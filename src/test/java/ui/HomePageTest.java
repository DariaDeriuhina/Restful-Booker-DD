package ui;

import io.qameta.allure.Description;
import org.testng.annotations.Test;
import ui.components.AlertMessage;
import ui.components.sections.RoomsSection;
import testdata.BookingTestData;
import testdata.ContactTestData;
import utils.ScrollUtils;
import utils.base.BaseUiTest;

import java.time.LocalDate;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static org.assertj.core.api.Assertions.assertThat;
import static utils.constants.TestGroups.REGRESSION;
import static utils.constants.TestGroups.UI;

public class HomePageTest extends BaseUiTest {

    @Description("BUG: Form submits with invalid Check In/Out dates")
    @Test(groups = {UI, REGRESSION}, dataProvider = "invalidBookingDates", dataProviderClass = BookingTestData.class)
    public void invalidBookingDatesShouldDisableBooking(String checkIn, String checkOut) {
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

    @Description("Verify All Section Titles")
    @Test(groups = {UI, REGRESSION})
    public void sectionTitlesTest() {
        assertThat(homePage.pageTitle().getText()).isEqualTo("Welcome to Shady Meadows B&B");
        homePage.booking().verifySectionTitle("Check Availability & Book Your Stay");
        homePage.rooms().verifySectionTitle("Our Rooms");
        homePage.contact().verifySectionTitle("Send Us a Message");
        homePage.location().verifySectionTitle("Our Location");
    }

    @Description("Verify scroll to sections via header")
    @Test(groups = {UI, REGRESSION})
    public void scrollToSectionsByHeaderTest() {
        homePage.header().clickRooms();
        ScrollUtils.assertAnchorHash("#rooms");

        homePage.header().clickBooking();
        ScrollUtils.assertAnchorHash("#booking");

        homePage.header().clickLocation();
        ScrollUtils.assertAnchorHash("#location");

        homePage.header().clickContact();
        ScrollUtils.assertAnchorHash("#contact");
    }

    @Description("Check Availability button leads to Rooms section validation")
    @Test(groups = {UI, REGRESSION}, dataProvider = "validBookingDates", dataProviderClass = BookingTestData.class)
    public void checkAvailabilityBtnLeadToRoomsTest(LocalDate checkIn, LocalDate checkOut) {
        var bookingSection = homePage.header.clickBooking();

        var roomSection = bookingSection.applyCheckInDate(checkIn)
                .applyCheckOutDate(checkOut)
                .clickOnCheckAvailability();

        var firstRoom = roomSection.getFirstRoomCard();
        firstRoom.bookNowBtn().shouldBe(clickable);
    }

    @Description("Contact form validation")
    @Test(groups = {UI, REGRESSION}, dataProvider = "contactFormData", dataProviderClass = ContactTestData.class)
    public void contactFormTest(String name, String email, String phone, String subject, String message, List<String> expectedWarnings) {
        var expectedConfirmationMessage = "Thanks for getting in touch %1$s! We'll get back to you about %2$s as soon as possible.".formatted(name, subject);
        var contactSection = homePage.contact()
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