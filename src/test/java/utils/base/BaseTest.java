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

@Slf4j
@Listeners({utils.RetryListener.class})
public abstract class BaseTest {

    protected EnvProperties env;

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
        env = new EnvProperties("env.properties");

        var runMode = System.getProperty("runMode", "local");
        var baseUrl = env.get("baseUrl", "https://automationintesting.online/");
        var remoteUrl = env.get("remoteUrl", "http://localhost:4444/wd/hub");

        Configuration.browser = browserName;
        Configuration.headless = headless;
        Configuration.baseUrl = baseUrl;

        if ("remote".equalsIgnoreCase(runMode)) {
            Configuration.remote = remoteUrl;
        } else {
            Configuration.remote = null;
        }

        GridStarter.setupGridForRemoteExecution();

        log.info("=== Test Configuration ===");
        log.info("Run mode: {}", runMode);
        log.info("Browser: {} | Headless: {}", browserName, headless);
        log.info("URL: {}", Configuration.remote != null ? Configuration.remote : "local");
        log.info("Base URL: {}", baseUrl);
        log.info("==========================");

        Selenide.open(baseUrl);

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