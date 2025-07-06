package test_data;

import api.models.BookingDates;
import api.models.BookingRequest;
import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

public class TestDataFactory {
    private final Faker faker = new Faker();
    private final AtomicInteger dateOffset = new AtomicInteger(0);

    public BookingRequest validBookingRequest() {
        int offset = 10 + dateOffset.getAndIncrement();
        var checkIn = LocalDate.now().plusDays(offset);
        var checkOut = checkIn.plusDays(1);

        return BookingRequest.builder()
                .roomid(faker.number().numberBetween(1, 10))
                .firstname(faker.name().firstName())
                .lastname(faker.name().lastName())
                .depositpaid(true)
                .email(faker.internet().emailAddress())
                .phone(faker.phoneNumber().cellPhone())
                .bookingdates(new BookingDates(checkIn.toString(), checkOut.toString()))
                .build();
    }

    public BookingRequest invalidBookingRequestEmptyFields() {
        return BookingRequest.builder()
                .roomid(1)
                .firstname("")
                .lastname("")
                .depositpaid(true)
                .email("")
                .phone("")
                .bookingdates(new BookingDates("2024-01-01", "2024-01-05"))
                .build();
    }

    public BookingRequest invalidBookingRequestInvalidEmail() {
        var validRequest = validBookingRequest();
        validRequest.setEmail("invalid-email");
        return validRequest;
    }

    public String maliciousInput() {
        return faker.options().option(
                "'; DROP TABLE u--",
                "admin' OR '1'='1",
                "Robert');--",
                "0 OR 1=1",
                "../etc/passwd",
                "admin\"--",
                "‘ OR ‘x’=’x"
        );
    }
}
