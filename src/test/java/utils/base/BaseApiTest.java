package utils.base;

import api.client.ApiClient;
import api.client.ApiConfig;
import api.services.AuthApiService;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import utils.EnvProperties;

import java.util.Map;

import static org.testng.Assert.assertEquals;

@Listeners({utils.RetryListener.class})
public abstract class BaseApiTest {

    protected static String authToken;

    @BeforeClass
    public void loginAndConfigureApiClient() {
        var env = new EnvProperties("env.properties");

        var username = env.get("username");
        var password = env.get("password");

        var response = new AuthApiService().login(username, password);
        assertEquals(response.statusCode(), 200, "Login failed, unexpected status code");

        authToken = response.jsonPath().getString("token");
        if (authToken == null || authToken.isBlank()) {
            throw new RuntimeException("Authentication token was not returned!");
        }

        var config = ApiConfig.builder()
                .defaultCookies(Map.of("token", authToken))
                .enableAllureReports(true)
                .build();
        ApiClient.setGlobalConfig(config);
    }
}