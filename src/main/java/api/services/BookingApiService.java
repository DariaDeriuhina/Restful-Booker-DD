package api.services;

import api.client.ApiClient;
import api.client.ApiRequest;
import api.client.ApiService;
import api.models.BookingRequest;
import io.restassured.http.Method;
import io.restassured.response.Response;

public class BookingApiService {
    private final ApiService api;

    public BookingApiService() {
        this(ApiClient.getInstance());
    }

    public BookingApiService(ApiService api) {
        this.api = api;
    }

    public Response createBooking(BookingRequest request) {
        return api.execute(
                ApiRequest.<Response>builder()
                        .method(Method.POST)
                        .endpoint("/booking")
                        .body(request)
                        .responseType(Response.class)
                        .build()
        );
    }
}