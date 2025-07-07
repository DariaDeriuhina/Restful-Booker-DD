package api.client;

import io.restassured.http.Method;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ApiRequest<T> {
    private final Method method;
    private final String endpoint;
    private final Object body;
    private final Map<String, String> headers;
    private final Map<String, Object> queryParams;
    private final Map<String, Object> pathParams;
    private final Class<T> responseType;

    private ApiRequest(Builder<T> builder) {
        this.method = builder.method;
        this.endpoint = builder.endpoint;
        this.body = builder.body;
        this.headers = builder.headers;
        this.queryParams = builder.queryParams;
        this.pathParams = builder.pathParams;
        this.responseType = builder.responseType;
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private Method method;
        private String endpoint;
        private Object body;
        private Map<String, String> headers = new HashMap<>();
        private Map<String, Object> queryParams = new HashMap<>();
        private Map<String, Object> pathParams = new HashMap<>();
        private Class<T> responseType;

        public Builder<T> method(Method method) {
            this.method = method;
            return this;
        }

        public Builder<T> endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder<T> body(Object body) {
            this.body = body;
            return this;
        }

        public Builder<T> headers(Map<String, String> headers) {
            this.headers = headers != null ? headers : new HashMap<>();
            return this;
        }

        public Builder<T> queryParams(Map<String, Object> queryParams) {
            this.queryParams = queryParams != null ? queryParams : new HashMap<>();
            return this;
        }

        public Builder<T> pathParams(Map<String, Object> pathParams) {
            this.pathParams = pathParams != null ? pathParams : new HashMap<>();
            return this;
        }

        public Builder<T> responseType(Class<T> responseType) {
            this.responseType = responseType;
            return this;
        }

        public Builder<T> withHeader(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        public Builder<T> withQueryParam(String key, Object value) {
            this.queryParams.put(key, value);
            return this;
        }

        public Builder<T> withPathParam(String key, Object value) {
            this.pathParams.put(key, value);
            return this;
        }

        public ApiRequest<T> build() {
            return new ApiRequest<>(this);
        }
    }
}