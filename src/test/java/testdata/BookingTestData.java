package testdata;

import api.models.BookingDates;
import org.testng.annotations.DataProvider;
import utils.DateUtils;
import utils.constants.TestConstants;

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

    @DataProvider(name = "maliciousInputProvider")
    public Object[][] maliciousInputProvider() {
        return new Object[][]{
                {"SQL Injection - DROP", "'; DROP TABLE u--"},
                {"SQL Injection - OR", "admin' OR '1'='1"},
                {"SQL Injection - Bobby", "Robert');--"},
                {"SQL Injection - Bool", "' OR 1=1--"},
                {"Path Traversal", "../etc/passwd"},
                {"XSS Script", "<script>alert()"},
                {"Command Injection", "; rm -rf /"},
                {"LDAP Injection", "*)(uid=*))(|(uid*"},
                {"XML Injection", "<?xml><foo>&xxe;"},
                {"NoSQL Injection", "{'$ne': null}"},
                {"Unicode bypass", "' OR 'x'='x"},
                {"Null byte", "admin\0"},
                {"HTML tags", "<h1>hack</h1>"},
                {"JS event", "onclick=alert(1)"},
                {"SQL comment", "admin'--"},
                {"Escape quote", "admin\"--"}
        };
    }

    @DataProvider(name = "bookingFormData")
    public Object[][] bookingFieldValidation() {
        return new Object[][]{
                // Firstname validation
                {
                        "Empty firstname",
                        "", TestConstants.DEFAULT_LASTNAME, true, TestConstants.TEST_EMAIL, TestConstants.TEST_PHONE, generateRandomDates(),
                        List.of("size must be between 3 and 18", "Firstname should not be blank")
                },
                {
                        "Firstname too short",
                        "An", TestConstants.DEFAULT_LASTNAME, true, TestConstants.TEST_EMAIL, TestConstants.TEST_PHONE, generateRandomDates(),
                        List.of("size must be between 3 and 18")
                },
                {
                        "Firstname at min boundary",
                        "Ann", TestConstants.DEFAULT_LASTNAME, true, TestConstants.TEST_EMAIL, TestConstants.TEST_PHONE, generateRandomDates(),
                        List.of()
                },
                {
                        "Firstname at max boundary",
                        "A".repeat(18), TestConstants.DEFAULT_LASTNAME, true, TestConstants.TEST_EMAIL, TestConstants.TEST_PHONE, generateRandomDates(),
                        List.of()
                },
                {
                        "Firstname too long",
                        "A".repeat(19), TestConstants.DEFAULT_LASTNAME, true, TestConstants.TEST_EMAIL, TestConstants.TEST_PHONE, generateRandomDates(),
                        List.of("size must be between 3 and 18")
                },

                // Lastname validation
                {
                        "Empty lastname",
                        TestConstants.DEFAULT_FIRSTNAME, "", true, TestConstants.TEST_EMAIL, TestConstants.TEST_PHONE, generateRandomDates(),
                        List.of("size must be between 3 and 30", "Lastname should not be blank")
                },
                {
                        "Lastname too short",
                        TestConstants.DEFAULT_FIRSTNAME, "Sm", true, TestConstants.TEST_EMAIL, TestConstants.TEST_PHONE, generateRandomDates(),
                        List.of("size must be between 3 and 30")
                },
                {
                        "Lastname at max boundary",
                        TestConstants.DEFAULT_FIRSTNAME, "S".repeat(30), true, TestConstants.TEST_EMAIL, TestConstants.TEST_PHONE, generateRandomDates(),
                        List.of()
                },
                {
                        "Lastname too long",
                        TestConstants.DEFAULT_FIRSTNAME, "S".repeat(31), true, TestConstants.TEST_EMAIL, TestConstants.TEST_PHONE, generateRandomDates(),
                        List.of("size must be between 3 and 30")
                },

                // Email validation
                {
                        "Empty email",
                        TestConstants.DEFAULT_FIRSTNAME, TestConstants.DEFAULT_LASTNAME, true, "", TestConstants.TEST_PHONE, generateRandomDates(),
                        List.of("must not be empty")
                },
                {
                        "Invalid email - no @",
                        TestConstants.DEFAULT_FIRSTNAME, TestConstants.DEFAULT_LASTNAME, true, "invalidemail", TestConstants.TEST_PHONE, generateRandomDates(),
                        List.of("must be a well-formed email address")
                },
                {
                        "Invalid email - no domain",
                        TestConstants.DEFAULT_FIRSTNAME, TestConstants.DEFAULT_LASTNAME, true, "test@", TestConstants.TEST_PHONE, generateRandomDates(),
                        List.of("must be a well-formed email address")
                },
                {
                        "Invalid email - no local part",
                        TestConstants.DEFAULT_FIRSTNAME, TestConstants.DEFAULT_LASTNAME, true, "@example.com", TestConstants.TEST_PHONE, generateRandomDates(),
                        List.of("must be a well-formed email address")
                },
                {
                        "Invalid email - double @",
                        TestConstants.DEFAULT_FIRSTNAME, TestConstants.DEFAULT_LASTNAME, true, "test@@example.com", TestConstants.TEST_PHONE, generateRandomDates(),
                        List.of("must be a well-formed email address")
                },

                // Phone validation
                {
                        "Empty phone",
                        TestConstants.DEFAULT_FIRSTNAME, TestConstants.DEFAULT_LASTNAME, true, TestConstants.TEST_EMAIL, "", generateRandomDates(),
                        List.of("must not be empty", "size must be between 11 and 21")
                },
                {
                        "Phone too short",
                        TestConstants.DEFAULT_FIRSTNAME, TestConstants.DEFAULT_LASTNAME, true, TestConstants.TEST_EMAIL, "1234567890", generateRandomDates(),
                        List.of("size must be between 11 and 21")
                },
                {
                        "Phone at min boundary",
                        TestConstants.DEFAULT_FIRSTNAME, TestConstants.DEFAULT_LASTNAME, true, TestConstants.TEST_EMAIL, TestConstants.DEFAULT_PHONE, generateRandomDates(),
                        List.of()
                },
                {
                        "Phone at max boundary",
                        TestConstants.DEFAULT_FIRSTNAME, TestConstants.DEFAULT_LASTNAME, true, TestConstants.TEST_EMAIL, "1".repeat(21), generateRandomDates(),
                        List.of()
                },
                {
                        "Phone too long",
                        TestConstants.DEFAULT_FIRSTNAME, TestConstants.DEFAULT_LASTNAME, true, TestConstants.TEST_EMAIL, "1".repeat(22), generateRandomDates(),
                        List.of("size must be between 11 and 21")
                },

                // Multiple errors
                {
                        "All fields empty",
                        "", "", true, "", "", generateRandomDates(),
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

                // Valid booking
                {
                        "All fields valid",
                        TestConstants.DEFAULT_FIRSTNAME, TestConstants.DEFAULT_LASTNAME, true, TestConstants.TEST_EMAIL, TestConstants.TEST_PHONE, generateRandomDates(),
                        List.of()
                }
        };
    }

    @DataProvider(name = "specialCharacters")
    public Object[][] specialCharacters() {
        return new Object[][]{
                {"O'Brien", "McDonald's"},
                {"Anne-Marie", "Smith-Jones"},
                {"José", "François"},
                {"Müller", "Schröder"}
        };
    }

    private static BookingDates generateRandomDates() {
        var checkIn = DateUtils.generateRandomDate(1, 365);
        var checkOut = checkIn.plusDays(1);
        return new BookingDates(checkIn.toString(), checkOut.toString());
    }
}
