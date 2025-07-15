package utils.base;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import utils.EnvProperties;
import utils.GridStarter;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static utils.EnvProperties.BASE_URL;

@Slf4j
@Listeners({utils.RetryListener.class})
public abstract class BaseTest {

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

        log.info("=== Test Configuration ===");
        log.info("Run mode: {}", runMode);
        log.info("Browser: {} | Headless: {}", browserName, headless);
        log.info("URL: {}", Configuration.remote != null ? Configuration.remote : "local");
        log.info("Base URL: {}", BASE_URL);
        log.info("==========================");
        Selenide.open(BASE_URL);

        if (!headless && Configuration.remote == null) {
            Selenide.webdriver().driver().getWebDriver().manage().window().maximize();
        }
    }

    @AfterMethod
    public void tearDown() {
        try {
            log.info("Closing browser after test.");
            closeWebDriver();
        } catch (Exception e) {
            log.warn("Error during tearDown: {}", e.getMessage());
        }
    }
}