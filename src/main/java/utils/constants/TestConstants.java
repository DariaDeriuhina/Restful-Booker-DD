package utils.constants;

import java.time.LocalDate;

public class TestConstants {
    public static final String DEFAULT_FIRSTNAME = "John";
    public static final String DEFAULT_LASTNAME = "Doe";
    public static final String DEFAULT_EMAIL = "john.doe@example.com";
    public static final String DEFAULT_PHONE = "12345678901";
    public static final String TEST_EMAIL = "anna@example.com";
    public static final String TEST_PHONE = "380123456789";
    public static final String CONTACT_SUBJECT = "Booking inquiry";
    public static final String CONTACT_MESSAGE = "I would like to book a room at your hotel.";
    public static final int DEFAULT_ROOM_ID = 1;
    public static final int DEFAULT_CHECKIN_OFFSET = 3;
    public static final int DEFAULT_BOOKING_DURATION = 1;
    public static final String WELCOME_TITLE = "Welcome to Shady Meadows B&B";
    public static final String BOOKING_SECTION_TITLE = "Check Availability & Book Your Stay";
    public static final String ROOMS_SECTION_TITLE = "Our Rooms";
    public static final String CONTACT_SECTION_TITLE = "Send Us a Message";
    public static final String LOCATION_SECTION_TITLE = "Our Location";
    public static LocalDate getDefaultCheckIn() {
        return LocalDate.now().plusDays(DEFAULT_CHECKIN_OFFSET);
    }

    public static LocalDate getDefaultCheckOut() {
        return getDefaultCheckIn().plusDays(DEFAULT_BOOKING_DURATION);
    }
}
