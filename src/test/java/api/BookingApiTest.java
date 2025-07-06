package api;

import api.models.BookingDates;
import api.models.BookingRequest;
import api.services.RoomApiService;
import api.testdata.DateRange;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import test_data.BookingTestData;
import utils.DateUtils;
import utils.base.BaseApiTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static utils.constants.TestGroups.API;
import static utils.constants.TestGroups.REGRESSION;

public class BookingApiTest extends BaseApiTest {

    private final BookingFacade bookingFacade = new BookingFacade();
    private final RoomApiService roomApi = new RoomApiService();

    @Description("Create booking and verify it blocks the room on selected dates")
    @Test(groups = {API, REGRESSION})
    public void roomIsUnavailableAfterBookingTest() {
        var checkIn = DateUtils.generateFutureDate(10);
        var checkOut = checkIn.plusDays(1);
        int roomId = new Random().nextInt(1, 9);

        var bookingResult = bookingFacade.bookDefault(roomId, checkIn, checkOut);
        assertThat(bookingResult.isSuccess()).isTrue();

        var availability = roomApi.getRoomAvailability(roomId);
        var isUnavailable = availability.stream()
                .anyMatch(item ->
                        item.getStart().equals(checkIn.toString()) &&
                                item.getEnd().equals(checkOut.toString()) &&
                                item.getTitle().equals("Unavailable")
                );
        assertThat(isUnavailable)
                .as("Expected unavailable date range to be present in availability report")
                .isTrue();

        var secondAttempt = bookingFacade.bookDefault(roomId, checkIn, checkOut);
        assertThat(secondAttempt.statusCode()).isEqualTo(500);
    }

    @Description("Multiple bookings on different dates should all appear in availability report")
    @Test(groups = {API, REGRESSION})
    public void multipleBookingsTest() {
        var roomId = new Random().nextInt(1, 9);;
        var dateRanges = List.of(
                new DateRange(10, 12),
                new DateRange(13, 15),
                new DateRange(16, 18)
        );

        for (var range : dateRanges) {
            var start = LocalDate.parse(range.start());
            var end = LocalDate.parse(range.end());
            var result = bookingFacade.bookDefault(roomId, start, end);
            assertThat(result.isSuccess()).isTrue();
        }

        var availabilityResponse = roomApi.getRoomAvailability(roomId);

        for (var range : dateRanges) {
            var found = availabilityResponse.stream().anyMatch(item ->
                    item.getStart().equals(range.start()) &&
                            item.getEnd().equals(range.end()) &&
                            item.getTitle().equals("Unavailable")
            );
            assertThat(found)
                    .as("Expected unavailable block from %s to %s", range.start(), range.end())
                    .isTrue();
        }
    }

    @Description("POST /booking without booking dates should not be completed")
    @Test(groups = {API, REGRESSION})
    public void bookingWithoutDatesTest() {
        int roomId = new Random().nextInt(1, 9);;
        var bookingResult = bookingFacade.bookRoomWithoutDates(roomId);
        assertThat(bookingResult.statusCode()).isEqualTo(500);
        assertThat(bookingResult.hasExactlyErrors(List.of("Failed to create booking"))).isTrue();
    }

    @Description("BUG: POST /booking should reject booking with check-in date in the past")
    @Test(groups = {API, REGRESSION})
    public void bookingInPastShouldBeRejectedTest() {
        int roomId = new Random().nextInt(1, 9);;
        var checkIn = DateUtils.generatePastDate(1, 100);
        var checkOut = checkIn.plusDays(1);

        var result = bookingFacade.bookDefault(roomId, checkIn, checkOut);
        assertThat(result.statusCode()).isEqualTo(400);
        assertThat(result.hasAnyError()).isFalse();
    }

    @Description("Only one of two simultaneous bookings for the same room and date should succeed")
    @Test(groups = {API, REGRESSION})
    public void concurrentBookingTest() throws Exception {
        int roomId = new Random().nextInt(1, 9);;
        var checkIn = DateUtils.generateFutureDate(10, 20);
        var checkOut = DateUtils.generateFutureDate(21, 30);

        var executor = Executors.newFixedThreadPool(2);
        try {
            List<Callable<Response>> tasks = List.of(
                    () -> bookingFacade.bookDefault(roomId, checkIn, checkOut).response(),
                    () -> bookingFacade.bookDefault(roomId, checkIn, checkOut).response()
            );

            var futures = executor.invokeAll(tasks);

            var threadResults = new Response[2];
            for (int i = 0; i < futures.size(); i++) {
                threadResults[i] = futures.get(i).get();
            }

            var successCount = Arrays.stream(threadResults)
                    .filter(response -> response.statusCode() == 200)
                    .count();

            var failureCount = Arrays.stream(threadResults)
                    .filter(response -> response.statusCode() != 200)
                    .count();

            assertThat(successCount).as("Only one booking should succeed").isEqualTo(1);
            assertThat(failureCount).as("Second booking should be rejected").isEqualTo(1);
        } finally {
            executor.shutdown();
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        }
    }

    @Description("BUG: API should not accept dangerous payloads in firstname")
    @Test(groups = {API, REGRESSION}, dataProvider = "maliciousInputProvider", dataProviderClass = BookingTestData.class)
    public void sqlInjectionBookingFormTest(String testCase, String maliciousInput) {
        int roomId = new Random().nextInt(1, 9);
        var checkIn = DateUtils.generatePastDate(10, 100);
        var checkOut = checkIn.plusDays(1);

        var result = bookingFacade.bookWithMaliciousName(roomId, checkIn, checkOut, maliciousInput);
        assertThat(result.statusCode()).isNotEqualTo(200);
    }

    @Description("Reservation form validation")
    @Test(groups = {API, REGRESSION}, dataProvider = "bookingFormData", dataProviderClass = BookingTestData.class)
    public void apiBookingValidationTest(String testCase, String firstname, String lastname, boolean depositpaid,
                                         String email, String phone, BookingDates bookingdates,
                                         List<String> expectedWarnings) {
        var roomid = new Random().nextInt(1, 9);
        var request = new BookingRequest(roomid, firstname, lastname, depositpaid, email, phone, bookingdates);
        var result = bookingFacade.book(request);

        if (expectedWarnings.isEmpty()) {
            assertThat(result.statusCode()).isEqualTo(200);
        } else {
            assertThat(result.statusCode()).isEqualTo(400);
            var actualErrors = result.getErrors();
            assertThat(actualErrors).containsExactlyInAnyOrderElementsOf(expectedWarnings);
        }
    }

    @Description("Special characters in names should be handled properly")
    @Test(groups = {API, REGRESSION}, dataProvider = "specialCharacters", dataProviderClass = BookingTestData.class)
    public void specialCharactersInNamesTest(String firstName, String lastName) {
        int roomId = new Random().nextInt(1, 9);;
        var checkIn = DateUtils.generateFutureDate(10, 100);
        var checkOut = checkIn.plusDays(1);
            var request = BookingRequest.builder()
                    .roomid(roomId)
                    .firstname(firstName)
                    .lastname(lastName)
                    .depositpaid(true)
                    .email("test@example.com")
                    .phone("12345678901")
                    .bookingdates(new BookingDates(
                            checkIn.toString(), checkOut.toString()
                    ))
                    .build();

            var result = bookingFacade.book(request);

            assertThat(result.isSuccess()).isTrue();
    }
}