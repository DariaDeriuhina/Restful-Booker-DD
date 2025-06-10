package api;

import api.services.RoomApiService;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RoomApiTest {
    private final RoomApiService roomApi = new RoomApiService();

    @Test(description = "GET /api/room should return a list of all rooms with required fields")
    public void getAllRoomsShouldContainExpectedFields() {
        var rooms = roomApi.getAllRooms();

        assertThat(rooms).isNotEmpty();
        for (var room : rooms) {
            assertThat(room.getRoomid()).isPositive();
            assertThat(room.getRoomName()).isNotBlank();
            assertThat(room.getType()).isNotBlank();
            assertThat(room.getRoomPrice()).isGreaterThan(0);
            assertThat(room.getFeatures()).isNotEmpty();
            assertThat(room.getImage()).isNotBlank();
            assertThat(room.getDescription()).isNotBlank();
            assertThat(room.isAccessible()).isIn(true, false);
        }
    }

    @Test(description = "GET /api/room/{id} should return correct details for known room")
    public void getRoomByIdShouldReturnCorrectData() {
        var room = roomApi.getRoomById(2);

        assertThat(room.getRoomid()).isEqualTo(2);
        assertThat(room.getRoomName()).isEqualTo("102");
        assertThat(room.getType()).isEqualTo("Double");
        assertThat(room.getRoomPrice()).isEqualTo(150);
        assertThat(room.getFeatures()).contains("TV", "Radio", "Safe");
        assertThat(room.isAccessible()).isTrue();
        assertThat(room.getImage()).isEqualTo("/images/room2.jpg");
    }
}
