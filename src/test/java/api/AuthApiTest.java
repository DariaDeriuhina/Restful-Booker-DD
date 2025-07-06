package api;

import api.services.AuthApiService;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Test;
import test_data.AuthTestData;
import utils.base.BaseApiTest;

import static org.assertj.core.api.Assertions.assertThat;
import static utils.constants.TestGroups.API;
import static utils.constants.TestGroups.REGRESSION;

public class AuthApiTest extends BaseApiTest {

    private final AuthApiService authApi = new AuthApiService();

    @Description("Invalid credentials should fail")
    @Test(groups = {API, REGRESSION}, dataProvider = "invalidAuthCases", dataProviderClass = AuthTestData.class)
    public void invalidCredentialsTest(String username, String password, String description) {
        var response = authApi.login(username, password);

        assertThat(response.statusCode())
                .as("%s should return 401", description)
                .isEqualTo(401);
    }

    @Description("Token validation should work correctly")
    @Test(groups = {API, REGRESSION}, dataProvider = "tokenValidationCases", dataProviderClass = AuthTestData.class)
    public void tokenValidationTest(String tokenType, int expectedStatus, String description) {
        var tokenToValidate = switch (tokenType) {
            case "valid" -> authToken;
            case "" -> "";
            default -> tokenType;
        };

        var response = authApi.validate(tokenToValidate);

        assertThat(response.statusCode())
                .as("Token '%s' should return %d", description, expectedStatus)
                .isEqualTo(expectedStatus);
    }
}