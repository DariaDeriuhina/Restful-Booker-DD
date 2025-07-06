package utils.base;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import ui.pages.BookingPage;
import ui.pages.HomePage;

public abstract class BaseUiTest extends BaseTest {

    protected HomePage homePage;
    protected BookingPage bookingPage;

    @Override
    @BeforeMethod
    @Parameters({"browserName", "headless"})
    public void setUp(String browserName, boolean headless) {
        super.setUp(browserName, headless);

        homePage = new HomePage();
        bookingPage = new BookingPage();
    }
}
