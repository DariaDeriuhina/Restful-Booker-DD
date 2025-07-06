package api.services;

import api.client.ApiClient;
import api.client.ApiRequest;
import api.models.BookingRequest;
import io.restassured.http.Method;
import io.restassured.response.Response;

public class BookingApiService {
    private final ApiClient apiClient = new ApiClient();

    public Response createBooking(BookingRequest request) {
        return apiClient.execute(
                ApiRequest.<Response>builder()
                        .method(Method.POST)
                        .endpoint("/booking")
                        .body(request)
                        .responseType(Response.class)
                        .build()
        );
    }
}