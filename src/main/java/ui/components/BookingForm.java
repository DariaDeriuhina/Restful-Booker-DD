package ui.components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import ui.elements.BaseElement;
import ui.pages.BookingPage;

import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selenide.$;

public class BookingForm extends BaseElement {
    private final SelenideElement firstNameField;
    private final SelenideElement lastNameField;
    private final SelenideElement emailField;
    private final SelenideElement phoneField;

    public BookingForm() {
        this.firstNameField = $("[placeholder='Firstname']");
        this.lastNameField = $("[placeholder='Lastname']");
        this.emailField = $("[placeholder='Email']");
        this.phoneField = $("[placeholder='Phone']");
    }

    @Step("Fill the form with: {}, {}, {}")
    public BookingPage fillForm(String firstName, String lastName, String email, String phone) {
        firstNameField.setValue(firstName);
        lastNameField.setValue(lastName);
        emailField.setValue(email);
        phoneField.setValue(phone);
        return new BookingPage();
    }

    public BookingForm assertFirstName(String expected) {
        firstNameField.shouldHave(value(expected));
        return this;
    }

    public BookingForm assertLastName(String expected) {
        lastNameField.shouldHave(value(expected));
        return this;
    }

    public BookingForm assertEmail(String expected) {
        emailField.shouldHave(value(expected));
        return this;
    }

    public BookingForm assertPhone(String expected) {
        phoneField.shouldHave(value(expected));
        return this;
    }
}
