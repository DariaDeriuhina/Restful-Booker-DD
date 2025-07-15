package api.client;

import io.restassured.response.Response;

import java.util.List;

public interface ApiService {
    <T> T execute(ApiRequest<T> request);

    <T> List<T> executeList(ApiRequest<Response> request, String jsonPath, Class<T> clazz);
}
