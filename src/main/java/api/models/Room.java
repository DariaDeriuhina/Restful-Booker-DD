package api.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Jacksonized
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
