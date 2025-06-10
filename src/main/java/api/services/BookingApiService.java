package api.services;

import api.BaseApi;
import api.models.BookingRequest;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BookingApiService extends BaseApi {

    public Response createBooking(BookingRequest bookingRequest) {
        return given()
                .spec(requestSpec())
                .body(bookingRequest)
                .post("/booking");
    }
}
