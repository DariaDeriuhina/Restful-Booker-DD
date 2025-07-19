package api;

import api.models.BookingDates;
import api.models.BookingRequest;
import api.services.RoomApiService;
import api.testdata.BookingRequestBuilder;
import api.testdata.BookingTestDataFactory;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import testdata.BookingTestData;
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
    protected int roomId;
    protected LocalDate checkIn;
    protected LocalDate checkOut;
    protected BookingDates bookingDates;

    @BeforeMethod
    public void setupTestData() {
        roomId = new Random().nextInt(1, 9);
        checkIn = DateUtils.generateRandomDate(1, 365);
        checkOut = checkIn.plusDays(1);
        bookingDates = new BookingDates(checkIn.toString(), checkOut.toString());
    }

    @Description("Create booking and verify it blocks the room on selected dates")
    @Test(groups = {API, REGRESSION})
    public void roomIsUnavailableAfterBookingTest() {
        var bookingResult = bookingFacade.bookDefault(roomId, bookingDates);
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

        var secondAttempt = bookingFacade.bookDefault(roomId, bookingDates);
        assertThat(secondAttempt.statusCode()).isEqualTo(500);
    }

    @Description("Multiple bookings on different dates should all appear in availability report")
    @Test(groups = {API, REGRESSION})
    public void multipleBookingsTest() {
        checkIn = DateUtils.generateFutureDate(100);
        checkOut = checkIn.plusDays(1);
        var dateRanges = List.of(
                new BookingDates(checkIn.toString(), checkOut.toString()),
                new BookingDates(checkIn.plusDays(1).toString(), checkOut.plusDays(1).toString()),
                new BookingDates(checkIn.plusDays(2).toString(), checkOut.plusDays(2).toString())
        );

        for (var range : dateRanges) {
            var result = bookingFacade.bookDefault(roomId, range);
            assertThat(result.isSuccess()).isTrue();
        }

        var availabilityResponse = roomApi.getRoomAvailability(roomId);

        for (var range : dateRanges) {
            var found = availabilityResponse.stream().anyMatch(item ->
                    item.getStart().equals(range.getCheckin()) &&
                            item.getEnd().equals(range.getCheckout()) &&
                            item.getTitle().equals("Unavailable")
            );
            assertThat(found)
                    .as("Expected unavailable block from %s to %s", range.getCheckin(), range.getCheckout())
                    .isTrue();
        }
    }

    @Description("POST /booking without booking dates should not be completed")
    @Test(groups = {API, REGRESSION})
    public void bookingWithoutDatesTest() {
        var bookingResult = bookingFacade.book(BookingTestDataFactory.requestWithoutDates(roomId));
        assertThat(bookingResult.statusCode()).isEqualTo(500);
        assertThat(bookingResult.hasExactlyErrors(List.of("Failed to create booking"))).isTrue();
    }

    @Description("BUG: POST /booking should reject booking with check-in date in the past")
    @Test(groups = {API, REGRESSION})
    public void bookingInPastShouldBeRejectedTest() {
        checkIn = DateUtils.generatePastDate(1, 100);
        checkOut = checkIn.plusDays(1);

        var result = bookingFacade.bookDefault(roomId, new BookingDates(checkIn.toString(), checkOut.toString()));
        assertThat(result.statusCode()).isEqualTo(400);
        assertThat(result.hasAnyError()).isFalse();
    }

    @Description("Only one of two simultaneous bookings for the same room and date should succeed")
    @Test(groups = {API, REGRESSION})
    public void concurrentBookingTest() throws Exception {
        var executor = Executors.newFixedThreadPool(2);
        try {
            List<Callable<Response>> tasks = List.of(
                    () -> bookingFacade.bookDefault(roomId, bookingDates).response(),
                    () -> bookingFacade.bookDefault(roomId, bookingDates).response()
            );

            var futures = executor.invokeAll(tasks);

            var threadResults = new Response[2];
            for (var i = 0; i < futures.size(); i++) {
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
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    @Description("BUG: API should not accept dangerous payloads in firstname")
    @Test(groups = {API, REGRESSION}, dataProvider = "maliciousInputProvider", dataProviderClass = BookingTestData.class)
    public void sqlInjectionBookingFormTest(String testCase, String maliciousInput) {
        var request = BookingTestDataFactory.requestWithMaliciousName(roomId, maliciousInput, checkIn, checkOut);
        var result = bookingFacade.book(request);
        assertThat(result.statusCode()).isNotEqualTo(200);
    }

    @Description("Reservation form validation")
    @Test(groups = {API, REGRESSION}, dataProvider = "bookingFormData", dataProviderClass = BookingTestData.class)
    public void apiBookingValidationTest(String testCase, String firstname, String lastname, boolean depositpaid,
                                         String email, String phone, BookingDates bookingdates,
                                         List<String> expectedWarnings) {
        var request = new BookingRequest(roomId, firstname, lastname, depositpaid, email, phone, bookingdates);
        var result = bookingFacade.book(request);

        if (expectedWarnings.isEmpty()) {
            assertThat(result.statusCode()).isEqualTo(200);
        } else {
            assertThat(result.statusCode()).isEqualTo(400);
            var actualErrors = result.getErrors();
            assertThat(actualErrors).containsExactlyInAnyOrderElementsOf(expectedWarnings);
        }
    }

    @Test(groups = {API, REGRESSION}, dataProvider = "specialCharacters", dataProviderClass = BookingTestData.class)
    public void specialCharactersInNamesTest(String firstName, String lastName) {
        var request = BookingRequestBuilder.builder()
                .forRoom(roomId)
                .by(firstName, lastName)
                .withEmail("test@example.com")
                .withPhone("12345678901")
                .withDeposit(true)
                .from(checkIn.toString())
                .to(checkOut.toString())
                .build();

        var result = bookingFacade.book(request);
        assertThat(result.isSuccess()).isTrue();
    }
}