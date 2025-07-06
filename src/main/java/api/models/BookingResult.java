package api.models;

import io.restassured.response.Response;

import java.util.Collections;
import java.util.List;

public record BookingResult(Response response) {

    public int statusCode() {
        return response.statusCode();
    }

    public boolean isSuccess() {
        return statusCode() == 200;
    }

    public boolean hasStatus(int expectedStatusCode) {
        return statusCode() == expectedStatusCode;
    }

    public List<String> getErrors() {
        try {
            return response.jsonPath().getList("errors", String.class);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public boolean hasExactlyErrors(List<String> expectedErrors) {
        return getErrors().equals(expectedErrors);
    }

    public boolean hasAnyError() {
        return !getErrors().isEmpty();
    }
}
