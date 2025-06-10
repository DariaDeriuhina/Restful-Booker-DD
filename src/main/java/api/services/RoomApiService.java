package api.services;

import api.BaseApi;
import api.models.BookingAvailability;
import api.models.Room;

import java.util.List;

import static io.restassured.RestAssured.given;

public class RoomApiService extends BaseApi {

    public List<Room> getAllRooms() {
        return given()
                .spec(requestSpec())
                .when()
                .get("/room")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("rooms", Room.class);
    }

    public Room getRoomById(int id) {
        return given()
                .spec(requestSpec())
                .when()
                .get("/room/" + id)
                .then()
                .statusCode(200)
                .extract()
                .as(Room.class);
    }

    public List<BookingAvailability> getRoomAvailability(int roomId) {
        return List.of(given()
                .spec(requestSpec())
                .when()
                .get("/report/room/" + roomId)
                .then()
                .statusCode(200)
                .extract()
                .as(BookingAvailability[].class));
    }
}
