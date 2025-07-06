package api.services;

import api.client.ApiClient;
import api.client.ApiRequest;
import api.models.BookingAvailability;
import api.models.Room;
import io.qameta.allure.Step;
import io.restassured.http.Method;
import io.restassured.response.Response;

import java.util.Arrays;
import java.util.List;

public class RoomApiService {
    private final ApiClient apiClient = new ApiClient();

    @Step("Get all rooms")
    public List<Room> getAllRooms() {
        return apiClient.executeForList(
                ApiRequest.<Response>builder()
                        .method(Method.GET)
                        .endpoint("/room")
                        .responseType(Response.class)
                        .build(),
                "rooms",
                Room.class
        );
    }

    @Step("Get room by {}")
    public Room getRoomById(int id) {
        return apiClient.execute(
                ApiRequest.<Room>builder()
                        .method(Method.GET)
                        .endpoint("/room/{id}")
                        .withPathParam("id", id)
                        .responseType(Room.class)
                        .build()
        );
    }

    public List<BookingAvailability> getRoomAvailability(int roomId) {
        BookingAvailability[] availability = apiClient.execute(
                ApiRequest.<BookingAvailability[]>builder()
                        .method(Method.GET)
                        .endpoint("/report/room/{roomId}")
                        .withPathParam("roomId", roomId)
                        .responseType(BookingAvailability[].class)
                        .build()
        );
        return Arrays.asList(availability);
    }
}