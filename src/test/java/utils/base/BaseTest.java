package utils.base;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.testng.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.EnvProperties;
import utils.GridStarter;

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
        var runMode = System.getProperty("runMode", "local");

        Configuration.browser = browserName;
        Configuration.headless = headless;
        Configuration.baseUrl = BASE_URL;

        if ("remote".equalsIgnoreCase(runMode)) {
            Configuration.remote = "http://localhost:4444/wd/hub";
        } else {
            Configuration.remote = null;
        }

        GridStarter.setupGridForRemoteExecution();

        logger.info("=== Test Configuration ===");
        logger.info("Run mode: {}", runMode);
        logger.info("Browser: {} | Headless: {}", browserName, headless);
        logger.info("URL: {}", Configuration.remote != null ? Configuration.remote : "local");
        logger.info("Base URL: {}", BASE_URL);
        logger.info("==========================");
        Selenide.open(BASE_URL);

        if (!headless && Configuration.remote == null) {
            Selenide.webdriver().driver().getWebDriver().manage().window().maximize();
        }
    }

    @AfterMethod
    public void tearDown() {
        try {
            logger.info("Closing browser after test.");
            closeWebDriver();
        } catch (Exception e) {
            logger.warn("Error during tearDown: {}", e.getMessage());
        }
    }
}