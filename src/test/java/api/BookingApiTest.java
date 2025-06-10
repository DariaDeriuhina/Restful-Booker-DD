package api;

import api.models.BookingDates;
import api.models.BookingRequest;
import api.testdata.DateRange;
import api.services.BookingApiService;
import api.services.RoomApiService;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import test_data.BookingTestData;
import utils.DateUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class BookingApiTest {

    private final BookingApiService bookingApi = new BookingApiService();
    private final RoomApiService roomApi = new RoomApiService();

    @Test(description = "Create booking and verify it blocks the room on selected dates")
    public void roomIsUnavailableAfterBookingTest() {
        var checkIn = DateUtils.generateFutureDate(10, 15);
        var checkOut = checkIn.plusDays(1);
        int roomId = 3;
        var bookingRequest = BookingRequest.builder()
                .roomid(roomId)
                .firstname("Test")
                .lastname("User")
                .depositpaid(false)
                .email("test@example.com")
                .phone("12345678901")
                .bookingdates(
                        BookingDates.builder()
                                .checkin(checkIn.toString())
                                .checkout(checkOut.toString())
                                .build()
                )
                .build();

        var bookingResponse = bookingApi.createBooking(bookingRequest);
        assertThat(bookingResponse.statusCode()).isEqualTo(200);

        var availability = roomApi.getRoomAvailability(roomId);
        var isUnavailable = availability.stream()
                .anyMatch(item ->
                        item.getStart().equals(checkIn.toString())
                                && item.getEnd().equals(checkOut.toString())
                                && item.getTitle().equals("Unavailable")
                );
        assertThat(isUnavailable)
                .as("Expected unavailable date range to be present in availability report")
                .isTrue();

        var secondAttempt = bookingApi.createBooking(bookingRequest);
        assertThat(secondAttempt.statusCode()).isEqualTo(500);
    }

    @Test(description = "Multiple bookings on different dates should all appear in availability report")
    public void multipleBookingsShouldBeUnavailableInReport() {
        int roomId = 3;

        var dateRanges = List.of(
                new DateRange(10, 12),
                new DateRange(13, 15),
                new DateRange(16, 18)
        );

        for (var range : dateRanges) {
            var bookingRequest = new BookingRequest(
                    roomId,
                    "Mass",
                    "Booking",
                    false,
                    "massbooking@example.com",
                    "12345678901",
                    new BookingDates(range.start(), range.end())
            );
            var response = bookingApi.createBooking(bookingRequest);
            assertThat(response.statusCode()).isEqualTo(200);
        }

        var availabilityResponse = roomApi.getRoomAvailability(roomId);

        for (var range : dateRanges) {
            var found = availabilityResponse.stream().anyMatch(a ->
                    a.getStart().equals(range.start())
                            && a.getEnd().equals(range.end())
                            && a.getTitle().equals("Unavailable")
            );

            assertThat(found)
                    .as("Expected unavailable block from %s to %s", range.start(), range.end())
                    .isTrue();
        }
    }

    @Test(description = "POST /booking without booking dates should not be completed")
    public void bookingWithoutDatesTest() {
        var roomId = 3;
        var bookingRequest = BookingRequest.builder()
                .roomid(roomId)
                .firstname("Test")
                .lastname("User")
                .depositpaid(false)
                .email("test@example.com")
                .phone("12345678901")
                .build();

        var bookingResponse = bookingApi.createBooking(bookingRequest);
        assertThat(bookingResponse.statusCode()).isEqualTo(500);

        var errorMessages = bookingResponse.jsonPath().getList("errors", String.class);
        assertThat(errorMessages).containsExactly("Failed to create booking");
    }

    @Test(description = "BUG: POST /booking should reject booking with check-in date in the past")
    public void bookingInPastShouldBeRejectedTest() {
        int roomId = 2;
        var checkIn = DateUtils.generatePastDate(1, 100);
        var checkOut = checkIn.plusDays(1);

        var bookingRequest = BookingRequest.builder()
                .roomid(roomId)
                .firstname("Past")
                .lastname("Tester")
                .depositpaid(false)
                .email("past@example.com")
                .phone("12345678901")
                .bookingdates(
                        BookingDates.builder()
                                .checkin(checkIn.toString())
                                .checkout(checkOut.toString())
                                .build()
                )
                .build();

        //Response is 200, this is a bug
        var response = bookingApi.createBooking(bookingRequest);
        assertThat(response.statusCode()).isEqualTo(400);

        var errors = response.jsonPath().getList("errors", String.class);
        assertThat(errors).isNotEmpty();
    }

    @Test(description = "Only one of two simultaneous bookings for the same room and date should succeed")
    public void concurrentBookingTest() throws Exception {
        int roomId = 2;
        var checkIn = DateUtils.generateFutureDate(10, 20).toString();
        var checkOut = DateUtils.generateFutureDate(21, 30).toString();

        var request1 = BookingRequest.builder()
                .roomid(roomId)
                .firstname("User1")
                .lastname("Parallel")
                .depositpaid(true)
                .email("user1@example.com")
                .phone("12345678901")
                .bookingdates(new BookingDates(checkIn, checkOut))
                .build();

        var request2 = BookingRequest.builder()
                .roomid(roomId)
                .firstname("User2")
                .lastname("Parallel")
                .depositpaid(true)
                .email("user2@example.com")
                .phone("12345678902")
                .bookingdates(new BookingDates(checkIn, checkOut))
                .build();

        var executor = Executors.newFixedThreadPool(2);
        try {
            List<Callable<Response>> tasks = List.of(
                    () -> bookingApi.createBooking(request1),
                    () -> bookingApi.createBooking(request2)
            );

            var futures = executor.invokeAll(tasks);

            var threadResults = new Response[2];
            for (int i = 0; i < futures.size(); i++) {
                threadResults[i] = futures.get(i).get();
            }

            var successCount = (int) Arrays.stream(threadResults)
                    .filter(response -> response.statusCode() == 200)
                    .count();

            var failureCount = (int) Arrays.stream(threadResults)
                    .filter(response -> response.statusCode() != 200)
                    .count();

            assertThat(successCount)
                    .as("Only one booking should succeed")
                    .isEqualTo(1);

            assertThat(failureCount)
                    .as("Second booking should be rejected")
                    .isEqualTo(1);
        } finally {
            executor.shutdown();
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        }
    }

    @Test(description = "BUG: API should not accept dangerous payloads in firstname",
            dataProvider = "maliciousInputProvider", dataProviderClass = BookingTestData.class)
    public void sqlInjectionBookingFormTest(String maliciousInput) {
        int roomId = 8;
        var checkIn = DateUtils.generatePastDate(10, 100);

        System.out.println(checkIn);
        var bookingRequest = BookingRequest.builder()
                .roomid(roomId)
                .firstname(maliciousInput)
                .lastname("Safe")
                .depositpaid(true)
                .email("safe@example.com")
                .phone("12345678901")
                .bookingdates(BookingDates.builder()
                        .checkin(checkIn.toString())
                        .checkout(checkIn.plusDays(1).toString())
                        .build())
                .build();

        var response = bookingApi.createBooking(bookingRequest);

        assertThat(response.statusCode())
                .as("Input [%s] should not be accepted as firstname", maliciousInput)
                .isNotEqualTo(200);
    }

    @Test(description = "Reservation form validation",
            dataProvider = "bookingFormData", dataProviderClass = BookingTestData.class)
    public void apiBookingValidationTest(int roomid, String firstname, String lastname, boolean depositpaid,
                                         String email, String phone, BookingDates bookingdates,
                                         List<String> expectedWarnings) {
        var request = new BookingRequest(roomid, firstname, lastname, depositpaid, email, phone, bookingdates);
        var response = bookingApi.createBooking(request);

        if (expectedWarnings.isEmpty()) {
            response.then().statusCode(200);
        } else {
            response.then().statusCode(400);
            var actualErrors = response.jsonPath().getList("errors");
            assertThat(actualErrors).containsAll(expectedWarnings);
            assertThat(actualErrors).hasSize(expectedWarnings.size());
        }
    }
}
