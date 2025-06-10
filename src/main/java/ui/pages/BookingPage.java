package ui.pages;

import com.codeborne.selenide.SelenideElement;

import java.time.LocalDate;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class BookingPage extends BasePage {

    private final SelenideElement firstNameField;
    private final SelenideElement lastNameField;
    private final SelenideElement emailField;
    private final SelenideElement phoneField;
    private final SelenideElement confirmationCardBodyDates;

    public BookingPage() {
        firstNameField = $("[placeholder='Firstname']");
        lastNameField = $("[placeholder='Lastname']");
        emailField = $("[placeholder='Email']");
        phoneField = $("[placeholder='Phone']");
        confirmationCardBodyDates = $(".booking-card").$(".text-center");
    }

    public BookingPage fillForm(String firstName, String lastName, String email, String phone) {
        firstNameField.setValue(firstName);
        lastNameField.setValue(lastName);
        emailField.setValue(email);
        phoneField.setValue(phone);
        return this;
    }

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

    public BookingPage openBookingPage(LocalDate checkIn, LocalDate checkOut, int roomId) {
        var url = "https://automationintesting.online/reservation/%d?checkin=%s&checkout=%s".formatted(roomId, checkIn, checkOut);
        openPage(url);
        return this;
    }

    public BookingPage assertFirstName(String expected) {
        firstNameField.shouldHave(value(expected).because("First name should persist after tab switch"));
        return this;
    }

    public BookingPage assertLastName(String expected) {
        lastNameField.shouldHave(value(expected).because("Last name should persist after tab switch"));
        return this;
    }

    public BookingPage assertEmail(String expected) {
        emailField.shouldHave(value(expected).because("Email should persist after tab switch"));
        return this;
    }

    public BookingPage assertPhone(String expected) {
        phoneField.shouldHave(value(expected).because("Phone should persist after tab switch"));
        return this;
    }

    public String getConfirmationDates() {
        return confirmationCardBodyDates.getText();
    }
}
