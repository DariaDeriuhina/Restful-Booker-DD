package ui.components.sections;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import lombok.experimental.Accessors;

import static com.codeborne.selenide.Selenide.$;

@Getter
@Accessors(fluent = true)
public class ContactSection extends BaseSection {

    private final SelenideElement nameField;
    private final SelenideElement emailField;
    private final SelenideElement phoneField;
    private final SelenideElement subjectField;
    private final SelenideElement messageField;
    private final SelenideElement submitBtn;
    private final SelenideElement confirmationCardBody;

    public ContactSection() {
        super($("#contact"));
        nameField = section.$("[data-testid='ContactName']");
        emailField = section.$("[data-testid='ContactEmail']");
        phoneField = section.$("[data-testid='ContactPhone']");
        subjectField = section.$("[data-testid='ContactSubject']");
        messageField = section.$("[data-testid='ContactDescription']");
        submitBtn = section.$("[type='button']");
        confirmationCardBody = section.$(".card-body");
    }

    public ContactSection fillForm(String name, String email, String phone, String subject, String message) {
        nameField.setValue(name);
        emailField.setValue(email);
        phoneField.setValue(phone);
        subjectField.setValue(subject);
        messageField.setValue(message);
        return this;
    }

    public ContactSection submit() {
        submitBtn.click();
        return this;
    }

    public String getGreetingText() {
        return confirmationCardBody.$("h3").getText();
    }

    public String getIntroText() {
        return confirmationCardBody.$$("p").get(0).getText();
    }

    public String getTopicText() {
        return confirmationCardBody.$$("p").get(1).getText();
    }

    public String getOutroText() {
        return confirmationCardBody.$$("p").get(2).getText();
    }

    public String getFullConfirmationMessage() {
        return getGreetingText() + " " + getIntroText() + " " + getTopicText() + " " + getOutroText();
    }
}
