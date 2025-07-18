package api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
public class BookingRequest {
    private int roomid;
    private String firstname;
    private String lastname;
    private boolean depositpaid;
    private String email;
    private String phone;
    private BookingDates bookingdates;
}
