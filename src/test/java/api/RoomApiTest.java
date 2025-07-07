package api;

import api.services.RoomApiService;
import api.models.Room;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.base.BaseApiTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static utils.constants.TestGroups.API;
import static utils.constants.TestGroups.REGRESSION;

public class RoomApiTest extends BaseApiTest {
    private final RoomApiService roomApi = new RoomApiService();

    @Description("GET /api/room should return a list of all rooms with required fields")
    @Test(groups = {API, REGRESSION})
    public void getAllRoomsShouldContainExpectedFields() {
        var rooms = roomApi.getAllRooms();
        assertThat(rooms).isNotEmpty();
        for (var room : rooms) {
            assertThat(room.getRoomid()).isPositive();
            assertThat(room.getRoomName()).isNotBlank();
            assertThat(room.getType()).isNotBlank();
            assertThat(room.getRoomPrice()).isGreaterThan(0);
            assertThat(room.getImage()).isNotBlank();
            assertThat(room.getDescription()).isNotBlank();
            assertThat(room.isAccessible()).isIn(true, false);
        }
    }

    @Description("GET /api/room/{id} should return correct details for known room")
    @Test(groups = {API, REGRESSION})
    public void getRoomByIdShouldReturnCorrectData() {
        var actualRoom = roomApi.getRoomById(2);
        var expectedRoom = Room.builder()
                .roomid(2)
                .roomName("102")
                .type("Double")
                .roomPrice(150)
                .features(List.of("TV", "Radio", "Safe"))
                .accessible(true)
                .image("/images/room2.jpg")
                .description("Vestibulum sollicitudin, lectus ac mollis consequat, lorem orci ultrices tellus, eleifend euismod tortor dui egestas erat. Phasellus et ipsum nisl. ")
                .build();

        assertThat(actualRoom).isEqualTo(expectedRoom);
    }
}
