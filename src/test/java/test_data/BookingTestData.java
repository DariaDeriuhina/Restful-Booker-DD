package test_data;

import api.models.BookingDates;
import org.testng.annotations.DataProvider;

import java.time.LocalDate;
import java.util.List;

public class BookingTestData {

    @DataProvider(name = "validBookingDates")
    public static Object[][] validDateRanges() {
        var today = LocalDate.now();
        return new Object[][]{
                {today, today.plusDays(1)},
                {today.plusDays(1), today.plusDays(7)}
        };
    }

    @DataProvider(name = "invalidBookingDates")
    public Object[][] invalidBookingDates() {
        return new Object[][]{
                {"06/07/2025", "05/07/2025"},
                {"01/01/2020", "05/01/2020"},
                {"", ""},
                {"g", "h"}
        };
    }

    @DataProvider(name = "bookingFormData")
    public Object[][] bookingFormData() {
        var roomId = 1;
        var depositPaid = true;
        var dates = new BookingDates("2024-01-01", "2024-01-05");

        return new Object[][]{
                {
                        roomId,
                        "Anna",
                        "Smith",
                        depositPaid,
                        "anna@example.com",
                        "380123456789",
                        dates,
                        List.of()
                },
                {
                        roomId,
                        "",
                        "",
                        depositPaid,
                        "",
                        "",
                        dates,
                        List.of(
                                "must not be empty",
                                "size must be between 3 and 18",
                                "size must be between 3 and 30",
                                "Firstname should not be blank",
                                "size must be between 11 and 21",
                                "Lastname should not be blank",
                                "must not be empty"
                        )
                },
                {
                        roomId,
                        "An",
                        "Smith",
                        depositPaid,
                        "anna@example.com",
                        "380123456789",
                        dates,
                        List.of("size must be between 3 and 18")
                },
                {
                        roomId,
                        "Anna",
                        "Sm",
                        depositPaid,
                        "anna@example.com",
                        "380123456789",
                        dates,
                        List.of("size must be between 3 and 30")
                },
                {
                        roomId,
                        "Anna",
                        "Smith",
                        depositPaid,
                        "",
                        "380123456789",
                        dates,
                        List.of("must not be empty")
                },
                {
                        roomId,
                        "Anna",
                        "Smith",
                        depositPaid,
                        "not-an-email",
                        "380123456789",
                        dates,
                        List.of("must be a well-formed email address")
                },
                {
                        roomId,
                        "Anna",
                        "Smith",
                        depositPaid,
                        "anna@example.com",
                        "",
                        dates,
                        List.of("must not be empty", "size must be between 11 and 21")
                },
                {
                        roomId,
                        "Anna",
                        "Smith",
                        depositPaid,
                        "anna@example.com",
                        "123",
                        dates,
                        List.of("size must be between 11 and 21")
                }
        };
    }

    @DataProvider(name = "maliciousInputProvider")
    public Object[][] maliciousInputProvider() {
        return new Object[][]{
                {"'; DROP TABLE u--"},          // classic drop
                {"admin' OR '1'='1"},           // auth bypass
                {"Robert');--"},                // Bobby Tables
                {"0 OR 1=1"},                   // boolean condition
                {"../etc/passwd"},             // path traversal
                {"admin\"--"},                  // escape quote
                {"‘ OR ‘x’=’x"},                // unicode quote
        };
    }

}
