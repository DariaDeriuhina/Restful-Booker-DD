package api.models;

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
public class BookingRequest {
    public int roomid;
    public String firstname;
    public String lastname;
    public boolean depositpaid;
    public String email;
    public String phone;
    public BookingDates bookingdates;
}
