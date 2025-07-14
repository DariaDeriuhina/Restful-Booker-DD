package utils;

import com.codeborne.selenide.Configuration;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.HttpURLConnection;
import java.net.URL;

public class GridStarter {
    private static final Logger logger = LoggerFactory.getLogger(GridStarter.class);

    public static void setupGridForRemoteExecution() {
        var runMode = System.getProperty("runMode", "local");
        if (!"remote".equalsIgnoreCase(runMode)) {
            logger.info("Local execution mode - skipping Grid setup");
            return;
        }

        logger.info("Setting up Selenium Grid for remote execution...");

        if (isGridRunning()) {
            logger.info("Grid is running");
        } else {
            logger.info("Grid not available - trying to start...");
            tryStartGrid();
        }

        setupChromeCapabilities();
        logger.info("Grid setup completed");
    }

    private static boolean isGridRunning() {
        try {
            var connection = (HttpURLConnection) new URL("http://localhost:4444/wd/hub/status").openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            return connection.getResponseCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    private static void tryStartGrid() {
        try {
            // Попробовать docker compose (новая версия) или docker-compose (старая)
            var commands = new String[][]{
                    {"docker", "compose", "up", "-d"},
                    {"docker-compose", "up", "-d"}
            };

            for (var command : commands) {
                try {
                    var process = new ProcessBuilder(command).start();
                    if (process.waitFor() == 0) {
                        logger.info("Grid startup command executed successfully");
                        return;
                    }
                } catch (Exception ignored) {}
            }

            logger.warn("Could not start Grid automatically. Please run: docker compose up -d");
        } catch (Exception e) {
            logger.warn("Could not start Grid automatically: {}", e.getMessage());
        }
    }

    private static void setupChromeCapabilities() {
        var options = new ChromeOptions();
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu");
        if (Configuration.headless) options.addArguments("--headless");

        var capabilities = new DesiredCapabilities();
        capabilities.merge(options);
        Configuration.browserCapabilities = capabilities;

        logger.info("Chrome capabilities configured for remote execution");
    }
}