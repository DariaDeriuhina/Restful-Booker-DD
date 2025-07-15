package api.services;

import api.client.ApiClient;
import api.client.ApiRequest;
import api.client.ApiService;
import io.qameta.allure.Step;
import io.restassured.http.Method;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class AuthApiService {
    private final ApiService api;

    public AuthApiService() {
        this(ApiClient.getInstance());
    }

    public AuthApiService(ApiService api) {
        this.api = api;
    }

    @Step("Login with api")
    public Response login(String username, String password) {
        var credentials = new HashMap<String, String>();
        credentials.put("username", username);
        credentials.put("password", password);

        return api.execute(
                ApiRequest.<Response>builder()
                        .method(Method.POST)
                        .endpoint("/auth/login")
                        .body(credentials)
                        .responseType(Response.class)
                        .build()
        );
    }

    public Response validate(String token) {
        var tokenBody = Map.of("token", token);

        return api.execute(
                ApiRequest.<Response>builder()
                        .method(Method.POST)
                        .endpoint("/auth/validate")
                        .body(tokenBody)
                        .responseType(Response.class)
                        .build()
        );
    }
}