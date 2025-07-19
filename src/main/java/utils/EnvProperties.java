package utils;

import java.io.IOException;
import java.util.Properties;

public class EnvProperties {
    private final Properties properties = new Properties();

    public EnvProperties(String fileName) {
        try (var input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new IllegalArgumentException("Properties file not found: " + fileName);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file: " + fileName, e);
        }
    }

    public String get(String key) {
        return get(key, null);
    }

    public String get(String key, String defaultValue) {
        var systemProperty = System.getProperty(key);
        return systemProperty != null ? systemProperty : properties.getProperty(key, defaultValue);
    }
}