package api.client;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class ApiConfig {
    @Builder.Default
    private String baseUrl = System.getProperty("baseUrl", "https://automationintesting.online/api");

    @Builder.Default
    private int connectionTimeout = 30000;

    @Builder.Default
    private int readTimeout = 30000;

    @Builder.Default
    private boolean enableLogging = true;

    @Builder.Default
    private boolean enableAllureReports = true;

    @Builder.Default
    private boolean enableMetrics = true;

    @Builder.Default
    private Map<String,String> defaultCookies = new HashMap<>();

}