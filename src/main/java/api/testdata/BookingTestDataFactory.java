package api.testdata;

import api.models.BookingRequest;
import java.time.LocalDate;

public class BookingTestDataFactory {
    public static BookingRequest requestWithoutDates(int roomId) {
        return BookingRequestBuilder.builder()
                .forRoom(roomId)
                .by("NoDate", "User")
                .withEmail("nodate@example.com")
                .withPhone("12345678901")
                .withoutDates()
                .build();
    }

    public static BookingRequest requestWithMaliciousName(int roomId, String name, LocalDate checkIn, LocalDate checkOut) {
        return BookingRequestBuilder.builder()
                .forRoom(roomId)
                .by(name, "Safe")
                .withEmail("safe@example.com")
                .withPhone("12345678901")
                .from(checkIn.toString())
                .to(checkOut.toString())
                .build();
    }
}