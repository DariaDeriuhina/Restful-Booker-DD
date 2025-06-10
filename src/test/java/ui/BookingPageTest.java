package ui;

import org.testng.annotations.Test;
import ui.pages.BookingPage;
import ui.pages.HomePage;
import utils.BrowserUtils;
import utils.ScrollUtils;
import utils.base.BaseTest;

import java.time.LocalDate;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static org.assertj.core.api.Assertions.assertThat;

public class BookingPageTest extends BaseTest {

    @Test(description = "BUG: Double click on 'Reserve Now' triggers client-side error due to race condition")
    public void doubleClickReserveNowTest() {
        var bookingPage = new BookingPage().openBookingPage(
                LocalDate.of(2025, 6, 5),
                LocalDate.of(2025, 6, 6),
                1
        );

        bookingPage
                .clickReserveNow()
                .fillForm("John", "Doe", "john@example.com", "380123456789")
                .doubleClickReserveNow();

        $("h2").shouldNotHave(text("Application error"));
    }

    @Test(description = "Reservation form validation")
    public void bookingFormTest() {
        var homePage = new HomePage();
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

    @Test(description = "Multi-Tab: Form data in Booking should persist after switching tabs")
    public void bookingFormDataPersistAfterTabSwitchTest() {
        var firstName = "John";
        var lastName = "Doe";
        var email = "john.doe@example.com";
        var phone = "12345678901";
        var checkIn = LocalDate.of(2025, 7, 20);
        var checkOut = LocalDate.of(2025, 7, 25);
        var roomId = 1;

        var booking = new BookingPage().openBookingPage(checkIn, checkOut, roomId)
                .clickReserveNow()
                .fillForm(firstName, lastName, email, phone);

        BrowserUtils.openNewTabAndReturn();

        booking.assertFirstName(firstName)
                .assertLastName(lastName)
                .assertEmail(email)
                .assertPhone(phone);
    }
}
