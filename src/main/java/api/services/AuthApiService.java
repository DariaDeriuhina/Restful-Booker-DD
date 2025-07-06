package api.services;

import api.client.ApiClient;
import api.client.ApiRequest;
import io.qameta.allure.Step;
import io.restassured.http.Method;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class AuthApiService {
    private final ApiClient apiClient = new ApiClient();

    @Step("Login with api")
    public Response login(String username, String password) {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", username);
        credentials.put("password", password);

        return apiClient.execute(
                ApiRequest.<Response>builder()
                        .method(Method.POST)
                        .endpoint("/auth/login")
                        .body(credentials)
                        .responseType(Response.class)
                        .build()
        );
    }

    public Response validate(String token) {
        Map<String, String> tokenBody = Map.of("token", token);

        return apiClient.execute(
                ApiRequest.<Response>builder()
                        .method(Method.POST)
                        .endpoint("/auth/validate")
                        .body(tokenBody)
                        .responseType(Response.class)
                        .build()
        );
    }
}