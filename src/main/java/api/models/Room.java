package api.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    private int roomid;
    private String roomName;
    private String type;
    private boolean accessible;
    private String image;
    private String description;
    private List<String> features;
    private int roomPrice;
}
