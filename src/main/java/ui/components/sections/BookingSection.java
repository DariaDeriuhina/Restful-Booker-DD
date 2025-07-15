package ui.components.sections;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;

@Getter
public class BookingSection extends BaseSection {
    private final SelenideElement checkInInput;
    private final SelenideElement checkOutInput;
    private final SelenideElement checkAvailabilityBtn;

    public BookingSection() {
        super($("#booking"));
        checkInInput = $x("//label[text()='Check In']/parent::div//input");
        checkOutInput = $x("//label[text()='Check Out']/parent::div//input");
        checkAvailabilityBtn = $x("//button[text()='Check Availability']");
    }

    public RoomsSection goToRoomsSectionViaButton(String buttonText) {
        clickButtonByText(buttonText);
        return new RoomsSection();
    }

    public BookingSection applyCheckInDate(LocalDate date) {
        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        var dateStr = date.format(formatter);
        executeJavaScript("arguments[0].value = arguments[1];", checkInInput, dateStr);
        return this;
    }

    public BookingSection applyCheckOutDate(LocalDate date) {
        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        var dateStr = date.format(formatter);
        executeJavaScript("arguments[0].value = arguments[1];", checkOutInput, dateStr);
        return this;
    }

    public BookingSection applyCheckInDate(String dateStr) {
        executeJavaScript("arguments[0].value = arguments[1];", checkInInput, dateStr);
        return this;
    }

    public BookingSection applyCheckOutDate(String dateStr) {
        executeJavaScript("arguments[0].value = arguments[1];", checkOutInput, dateStr);
        return this;
    }

    public RoomsSection clickOnCheckAvailability() {
        scrollToElementCenter(checkAvailabilityBtn);
        checkAvailabilityBtn.click();
        return new RoomsSection();
    }
}
