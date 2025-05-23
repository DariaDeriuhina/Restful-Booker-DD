package utils.base;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.EnvProperties;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static utils.EnvProperties.BASE_URL;

public abstract class BaseTest {
    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

    @BeforeMethod
    @Parameters({"browserName", "headless"})
    public void setUp(String browserName, boolean headless) {
        EnvProperties.setUpInstance();
        Configuration.browser = browserName;
        Configuration.headless = headless;
        Configuration.baseUrl = BASE_URL;

        logger.info("Starting test with browser: {} | Headless: {}", browserName, headless);
        Selenide.open(EnvProperties.BASE_URL);
    }

    @AfterMethod
    public void tearDown() {
        logger.info("Closing browser after test.");
        closeWebDriver();
    }
}