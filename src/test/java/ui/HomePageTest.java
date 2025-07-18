package ui;

import io.qameta.allure.Description;
import org.testng.annotations.Test;
import ui.components.AlertMessage;
import ui.components.sections.RoomsSection;
import testdata.BookingTestData;
import testdata.ContactTestData;
import utils.assertions.UiAssertions;
import utils.base.BaseUiTest;
import utils.constants.TestConstants;

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
        assertThat(homePage.pageTitle().getText()).isEqualTo(TestConstants.WELCOME_TITLE);
        homePage.booking().verifySectionTitle(TestConstants.BOOKING_SECTION_TITLE);
        homePage.rooms().verifySectionTitle(TestConstants.ROOMS_SECTION_TITLE);
        homePage.contact().verifySectionTitle(TestConstants.CONTACT_SECTION_TITLE);
        homePage.location().verifySectionTitle(TestConstants.LOCATION_SECTION_TITLE);
    }

    @Description("Verify scroll to sections via header")
    @Test(groups = {UI, REGRESSION})
    public void scrollToSectionsByHeaderTest() {
        homePage.header().clickRooms();
        UiAssertions.assertAnchorHash("#rooms");

        homePage.header().clickBooking();
        UiAssertions.assertAnchorHash("#booking");

        homePage.header().clickLocation();
        UiAssertions.assertAnchorHash("#location");

        homePage.header().clickContact();
        UiAssertions.assertAnchorHash("#contact");
    }

    @Description("Check Availability button leads to Rooms section validation")
    @Test(groups = {UI, REGRESSION}, dataProvider = "validBookingDates", dataProviderClass = BookingTestData.class)
    public void checkAvailabilityBtnLeadToRoomsTest(LocalDate checkIn, LocalDate checkOut) {
        var bookingSection = homePage.header().clickBooking();

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