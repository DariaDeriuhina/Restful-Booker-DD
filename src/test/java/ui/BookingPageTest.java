package ui;

import io.qameta.allure.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.BrowserUtils;
import utils.assertions.UiAssertions;
import utils.base.BaseUiTest;
import utils.constants.TestConstants;

import java.time.LocalDate;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static org.assertj.core.api.Assertions.assertThat;
import static utils.constants.TestGroups.*;

public class BookingPageTest extends BaseUiTest {
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String phone;
    protected LocalDate checkIn;
    protected LocalDate checkOut;
    protected int roomId;

    @BeforeMethod
    public void setupBookingTestData() {
        firstName = TestConstants.DEFAULT_FIRSTNAME;
        lastName = TestConstants.DEFAULT_LASTNAME;
        email = TestConstants.DEFAULT_EMAIL;
        phone = TestConstants.DEFAULT_PHONE;
        checkIn = TestConstants.getDefaultCheckIn();
        checkOut = TestConstants.getDefaultCheckOut();
        roomId = TestConstants.DEFAULT_ROOM_ID;
    }

    @Description("BUG: Double click on 'Reserve Now' triggers client-side error due to race condition")
    @Test(groups = {UI, REGRESSION})
    public void doubleClickReserveNowTest() {
        var page = bookingPage.openBookingPage(checkIn, checkOut, roomId);

        page.clickReserveNow()
                .bookingForm()
                .fillForm(firstName, lastName, email, phone);
        page.doubleClickReserveNow();

        $("h2").shouldNotHave(text("Application error"));
    }

    @Description("Reservation form validation")
    @Test(groups = {UI, REGRESSION})
    public void bookingFormTest() {
        var bookingForm = homePage.rooms()
                .getFirstRoomCard()
                .clickBookNow();
        bookingForm.clickReserveNow()
                .bookingForm()
                .fillForm(firstName, lastName, email, phone);
        bookingForm.clickReserveNow();

        assertThat(bookingForm.getConfirmationDates()).isEqualTo(LocalDate.now() + " - " + LocalDate.now().plusDays(1));
        bookingForm.clickReturnHome();
        UiAssertions.assertAnchorHash("");
    }

    @Description("Multi-Tab: Form data in Booking should persist after switching tabs")
    @Test(groups = {UI, REGRESSION})
    public void bookingFormDataPersistAfterTabSwitchTest() {
        var booking = bookingPage.openBookingPage(checkIn, checkOut, roomId)
                .clickReserveNow()
                .bookingForm()
                .fillForm(firstName, lastName, email, phone);

        BrowserUtils.openNewTabAndReturn();

        booking.assertFirstName(firstName)
                .assertLastName(lastName)
                .assertEmail(email)
                .assertPhone(phone);
    }
}
