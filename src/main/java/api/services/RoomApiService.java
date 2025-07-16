package api.services;

import api.client.ApiClient;
import api.client.ApiRequest;
import api.client.ApiService;
import api.models.BookingAvailability;
import api.models.Room;
import io.qameta.allure.Step;
import io.restassured.http.Method;
import io.restassured.response.Response;

import java.util.List;

public class RoomApiService {
    private final ApiService api;

    public RoomApiService() {
        this(ApiClient.getInstance());
    }

    public RoomApiService(ApiService api) {
        this.api = api;
    }

    @Step("Get all rooms")
    public List<Room> getAllRooms() {
        return api.executeList(
                ApiRequest.<Response>builder()
                        .method(Method.GET)
                        .endpoint("/room")
                        .responseType(Response.class)
                        .build(),
                "rooms",
                Room.class
        );
    }

    @Step("Get room by id = {id}")
    public Room getRoomById(int id) {
        return api.execute(
                ApiRequest.<Room>builder()
                        .method(Method.GET)
                        .endpoint("/room/{id}")
                        .withPathParam("id", id)
                        .responseType(Room.class)
                        .build()
        );
    }

    public List<BookingAvailability> getRoomAvailability(int roomId) {
        var availability = api.execute(
                ApiRequest.<BookingAvailability[]>builder()
                        .method(Method.GET)
                        .endpoint("/report/room/{roomId}")
                        .withPathParam("roomId", roomId)
                        .responseType(BookingAvailability[].class)
                        .build()
        );
        return List.of(availability);
    }
}