package api;

import api.models.*;
import api.services.BookingApiService;
import api.testdata.BookingRequestBuilder;
import io.qameta.allure.Step;

import java.time.LocalDate;

public class BookingFacade {
    private final BookingApiService bookingApi = new BookingApiService();

    public BookingResult book(UserInfo user, int roomId, BookingDates period) {
        var request = BookingRequestBuilder.newRequest()
                .forRoom(roomId)
                .by(user.firstname(), user.lastname())
                .withEmail(user.email())
                .withPhone(user.phone())
                .from(period.getCheckin())
                .to(period.getCheckout())
                .build();

        return send(request);
    }

    @Step("Book the room with api")
    public BookingResult bookDefault(int roomId, LocalDate checkIn, LocalDate checkOut) {
        var user = new UserInfo("Default", "User", "default@example.com", "12345678901");
        var period = new BookingDates(checkIn.toString(), checkOut.toString());
        return book(user, roomId, period);
    }

    @Step("Book the room with api without dates")
    public BookingResult bookRoomWithoutDates(int roomId) {
        var request = BookingRequestBuilder.newRequest()
                .forRoom(roomId)
                .by("NoDate", "User")
                .withEmail("nodate@example.com")
                .withPhone("12345678901")
                .withDeposit(false)
                .withoutDates()
                .build();

        return send(request);
    }

    @Step("Book the room with api with malicious name")
    public BookingResult bookWithMaliciousName(int roomId, LocalDate checkIn, LocalDate checkOut, String maliciousName) {
        var request = BookingRequestBuilder.newRequest()
                .forRoom(roomId)
                .by(maliciousName, "Safe")
                .withEmail("safe@example.com")
                .withPhone("12345678901")
                .withDeposit(true)
                .from(checkIn.toString())
                .to(checkOut.toString())
                .build();

        return send(request);
    }

    public BookingResult book(BookingRequest request) {
        return send(request);
    }

    private BookingResult send(BookingRequest request) {
        var response = bookingApi.createBooking(request);
        return new BookingResult(response);
    }
}