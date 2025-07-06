package ui;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.BrowserUtils;
import utils.ScrollUtils;
import utils.base.BaseUiTest;

import java.time.LocalDate;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static org.assertj.core.api.Assertions.assertThat;
import static utils.constants.TestGroups.*;

public class BookingPageTest extends BaseUiTest {

    @Description("BUG: Double click on 'Reserve Now' triggers client-side error due to race condition")
    @Test(groups = {UI, REGRESSION})
    public void doubleClickReserveNowTest() {
        var page = bookingPage.openBookingPage(
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                1
        );

        page.clickReserveNow()
                .fillForm("John", "Doe", "john@example.com", "380123456789")
                .doubleClickReserveNow();

        $("h2").shouldNotHave(text("Application error"));
    }

    @Description("Reservation form validation")
    @Test(groups = {UI, REGRESSION})
    public void bookingFormTest() {
        var bookingForm = homePage.rooms()
                .getFirstRoomCard()
                .clickBookNow();
        bookingForm.clickReserveNow()
                .fillForm("John", "Doe", "john@example.com", "380123456789")
                .clickReserveNow();

        assertThat(bookingForm.getConfirmationDates()).isEqualTo(LocalDate.now() + " - " + LocalDate.now().plusDays(1));
        bookingForm.clickReturnHome();
        ScrollUtils.assertAnchorHash("");
    }

    @Description("Multi-Tab: Form data in Booking should persist after switching tabs")
    @Test(groups = {UI, REGRESSION})
    public void bookingFormDataPersistAfterTabSwitchTest() {
        var firstName = "John";
        var lastName = "Doe";
        var email = "john.doe@example.com";
        var phone = "12345678901";
        var checkIn = LocalDate.now().plusDays(3);
        var checkOut = checkIn.plusDays(1);
        var roomId = 1;

        var booking = bookingPage.openBookingPage(checkIn, checkOut, roomId)
                .clickReserveNow()
                .fillForm(firstName, lastName, email, phone);

        BrowserUtils.openNewTabAndReturn();

        booking.assertFirstName(firstName)
                .assertLastName(lastName)
                .assertEmail(email)
                .assertPhone(phone);
    }
}
