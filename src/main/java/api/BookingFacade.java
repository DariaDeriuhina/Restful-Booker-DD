package api;

import api.models.BookingDates;
import api.models.BookingRequest;
import api.models.BookingResult;
import api.models.UserInfo;
import api.services.BookingApiService;
import api.testdata.BookingRequestBuilder;
import io.qameta.allure.Step;

public class BookingFacade {
    private final BookingApiService bookingApi = new BookingApiService();

    @Step("Book the room with custom user and dates")
    public BookingResult book(UserInfo user, int roomId, BookingDates period) {
        var request = BookingRequestBuilder.builder()
                .forRoom(roomId)
                .by(user.firstname(), user.lastname())
                .withEmail(user.email())
                .withPhone(user.phone())
                .from(period.getCheckin())
                .to(period.getCheckout())
                .build();

        return send(request);
    }

    @Step("Book the room with given request")
    public BookingResult book(BookingRequest request) {
        return send(request);
    }

    @Step("Book the room with default user")
    public BookingResult bookDefault(int roomId, BookingDates period) {
        var user = new UserInfo("Default", "User", "default@example.com", "12345678901");
        return book(user, roomId, period);
    }

    private BookingResult send(BookingRequest request) {
        var response = bookingApi.createBooking(request);
        return new BookingResult(response);
    }
}