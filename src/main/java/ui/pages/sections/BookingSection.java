package ui.pages.sections;

import static com.codeborne.selenide.Selenide.$;

public class BookingSection extends BaseSection {

    public BookingSection() {
        super($("#booking"));
    }

    public void verifyTitle(String title) {
        verifySectionTitle(title);
    }

    protected RoomsSection goToRoomsSectionViaButton(String buttonText) {
        clickButtonByText(buttonText);
        return new RoomsSection();
    }
}
