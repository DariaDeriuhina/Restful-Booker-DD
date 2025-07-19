package ui.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.experimental.Accessors;
import ui.components.BookingForm;

import java.time.LocalDate;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Getter
@Accessors(fluent = true)
public class BookingPage extends BasePage {

    private final BookingForm bookingForm;
    private final SelenideElement confirmationCardBodyDates;

    public BookingPage() {
        this.bookingForm = new BookingForm();
        confirmationCardBodyDates = $(".booking-card").$(".text-center");
    }

    @Step("Click Reserve Now")
    public BookingPage clickReserveNow() {
        clickButtonByText("Reserve Now");
        return this;
    }

    public void doubleClickReserveNow() {
        doubleClickButtonByText("Reserve Now");
    }

    public void clickReturnHome() {
        doubleClickButtonByText("Return home");
    }

    protected void doubleClickButtonByText(String buttonText) {
        $$("button").findBy(text(buttonText)).shouldBe(visible).doubleClick();
    }

    @Step("Open booking page")
    public BookingPage openBookingPage(LocalDate checkIn, LocalDate checkOut, int roomId) {
        var url = env.get("bookingUrl", "https://automationintesting.online/reservation/%d?checkin=%s&checkout=%s").formatted(roomId, checkIn, checkOut);
        openPage(url);
        return this;
    }
    public String getConfirmationDates() {
        scrollToElementCenter(confirmationCardBodyDates);
        return confirmationCardBodyDates.getText();
    }
}
