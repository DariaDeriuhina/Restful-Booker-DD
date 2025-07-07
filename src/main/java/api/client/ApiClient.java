package api.client;

import api.client.interceptors.MetricsInterceptor;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

@Slf4j
public class ApiClient {
    @Setter
    private static ApiConfig globalConfig;

    private final ApiConfig config;
    private final RequestSpecification baseSpec;
    private final List<Filter> filters;

    public ApiClient() {
        this(globalConfig != null ? globalConfig : ApiConfig.builder().build());
    }

    public ApiClient(ApiConfig config) {
        this.config = config;
        this.filters = setupFilters();
        this.baseSpec = buildBaseSpec();
        configureRestAssured();
    }

    private List<Filter> setupFilters() {
        List<Filter> filterList = new ArrayList<>();
        if (config.isEnableMetrics()) {
            filterList.add(new MetricsInterceptor());
        }
        return filterList;
    }

    private RequestSpecification buildBaseSpec() {
        var builder = new RequestSpecBuilder()
                .setBaseUri(config.getBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON);

        if (config.getDefaultCookies() != null) {
            config.getDefaultCookies().forEach((k, v) -> builder.addHeader("Cookie", k + "=" + v));
        }
        builder.addFilter(new AllureRestAssured());
        filters.forEach(builder::addFilter);
        return builder.build();
    }

    private void configureRestAssured() {
        RestAssured.config = RestAssured.config()
                .httpClient(RestAssured.config().getHttpClientConfig()
                        .setParam("http.connection.timeout", config.getConnectionTimeout())
                        .setParam("http.socket.timeout", config.getReadTimeout()));
    }

    @SuppressWarnings("unchecked")
    public <T> T execute(ApiRequest<T> request) {
        var spec = given().spec(baseSpec);

        if (!request.getHeaders().isEmpty()) {
            spec.headers(request.getHeaders());
        }
        if (!request.getQueryParams().isEmpty()) {
            spec.queryParams(request.getQueryParams());
        }
        if (!request.getPathParams().isEmpty()) {
            spec.pathParams(request.getPathParams());
        }
        if (request.getBody() != null) {
            spec.body(request.getBody());
        }

        var response = spec.request(request.getMethod(), request.getEndpoint());
        if (request.getResponseType() != null && request.getResponseType() != Response.class) {
            return response.as(request.getResponseType());
        }
        return (T) response;
    }

    public <T> List<T> executeForList(ApiRequest<Response> request, String jsonPath, Class<T> clazz) {
        var response = this.execute(request);
        return response.jsonPath().getList(jsonPath, clazz);
    }

}