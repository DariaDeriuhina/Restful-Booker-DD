package utils;

import com.codeborne.selenide.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
public class GridStarter {

    public static void setupGridForRemoteExecution() {
        var runMode = System.getProperty("runMode", "local");
        if (!"remote".equalsIgnoreCase(runMode)) {
            log.info("Local execution mode - skipping Grid setup");
            return;
        }

        log.info("Setting up Selenium Grid for remote execution...");

        if (isGridRunning()) {
            log.info("Grid is running");
        } else {
            log.info("Grid not available - trying to start...");
            tryStartGrid();
        }

        setupChromeCapabilities();
        log.info("Grid setup completed");
    }

    private static boolean isGridRunning() {
        var gridStatusUrl = new EnvProperties("env.properties").get("seleniumUrl", "http://localhost:4444/wd/hub/status");
        try {
            var connection = (HttpURLConnection) new URL(gridStatusUrl).openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            return connection.getResponseCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    private static void tryStartGrid() {
        try {
            var process = new ProcessBuilder("docker", "compose", "up", "-d").start();
            if (process.waitFor() == 0) {
                log.info("Grid startup command executed successfully");
            } else {
                log.warn("Failed to start Grid. Exit code: {}", process.exitValue());
            }
        } catch (Exception e) {
            log.warn("Could not start Grid automatically: {}", e.getMessage());
            log.warn("Please run manually: docker compose up -d");
        }
    }

    private static void setupChromeCapabilities() {
        var options = new ChromeOptions();
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu");
        if (Configuration.headless) options.addArguments("--headless");

        var capabilities = new DesiredCapabilities();
        capabilities.merge(options);
        Configuration.browserCapabilities = capabilities;

        log.info("Chrome capabilities configured for remote execution");
    }
}