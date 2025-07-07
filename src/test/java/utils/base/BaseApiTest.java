package utils.base;

import api.client.ApiClient;
import api.client.ApiConfig;
import api.services.AuthApiService;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

import java.util.Map;

@Listeners({utils.RetryListener.class})
public abstract class BaseApiTest {
    protected static String authToken;

    @BeforeClass
    public void loginAndConfigureApiClient() {
        var response = new AuthApiService().login("admin", "password");
        authToken = response.jsonPath().getString("token");

        var config = ApiConfig.builder()
                .defaultCookies(Map.of("token", authToken))
                .enableAllureReports(true)
                .build();
        ApiClient.setGlobalConfig(config);
    }
}