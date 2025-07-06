package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static java.util.Objects.isNull;

public class EnvProperties {
    public static final String PATH_TO_RESOURCES = "src/main/resources/";
    public static final String PROPERTIES_FILE_NAME = "env.properties";
    private static Properties properties = new Properties();
    public static String BASE_URL;

    public static void setUpInstance() {
        var filePath = PATH_TO_RESOURCES + PROPERTIES_FILE_NAME;
        try {
            loadProperties(filePath);
        } catch (Exception ignored) {
        }
        BASE_URL = getProperty("baseUrl", "https://automationintesting.online/");
    }


    private static void loadProperties(String filePath) {
        try (var fis = new FileInputStream(filePath)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file: " + filePath, e);
        }
    }

    public static String getProperty(String propertyName, String defaultValue) {
        var systemProperty = System.getProperty(propertyName);
        return !isNull(systemProperty) ? systemProperty : properties.getProperty(propertyName, defaultValue);
    }
}
