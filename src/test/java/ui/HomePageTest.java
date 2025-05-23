package ui;

import com.codeborne.selenide.Selenide;
import org.testng.annotations.Test;
import ui.pages.HomePage;
import utils.base.BaseTest;

import static org.assertj.core.api.Assertions.assertThat;

public class HomePageTest extends BaseTest {

    @Test(description = "Verify All Section Titles")
    public void sectionTitlesTest() {
        var homePage = new HomePage();
        assertThat(homePage.pageTitle().getText()).isEqualTo("Welcome to Shady Meadows B&B");
        homePage.booking().verifyTitle("Check Availability & Book Your Stay");
        homePage.rooms().verifyTitle("Our Rooms");
        homePage.contact().verifyTitle("Send Us a Message");
        homePage.location().verifyTitle("Our Location");
    }
}

