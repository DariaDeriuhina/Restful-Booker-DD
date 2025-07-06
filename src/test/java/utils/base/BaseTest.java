package utils.base;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.testng.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.EnvProperties;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static utils.EnvProperties.BASE_URL;

@Listeners({utils.RetryListener.class})
public abstract class BaseTest {
    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

    @BeforeSuite
    public void setUpAllure() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(false)
                .includeSelenideSteps(false));
    }


    @BeforeMethod
    @Parameters({"browserName", "headless"})
    public void setUp(String browserName, boolean headless) {
        EnvProperties.setUpInstance();
        Configuration.browser = browserName;
        Configuration.headless = headless;
        Configuration.baseUrl = BASE_URL;

        logger.info("Starting test with browser: {} | Headless: {}", browserName, headless);
        Selenide.open(EnvProperties.BASE_URL);
        Selenide.webdriver().driver().getWebDriver().manage().window().maximize();
    }

    @AfterMethod
    public void tearDown() {
        logger.info("Closing browser after test.");
        closeWebDriver();
    }
}