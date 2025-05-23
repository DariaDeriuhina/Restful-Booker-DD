package ui.pages.sections;

import static com.codeborne.selenide.Selenide.$;

public class ContactSection extends BaseSection {

    public ContactSection() {
        super($("#contact"));
    }
    public void verifyTitle(String title) {
        verifySectionTitle(title);
    }
}
