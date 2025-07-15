package utils;

import api.models.BookingDates;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

public class DateUtils {

    public static LocalDate generateRandomDate(int minOffset, int maxOffset) {
        if (minOffset > maxOffset) {
            throw new IllegalArgumentException("minOffset must be less than or equal to maxOffset");
        }

        int randomDays = ThreadLocalRandom.current().nextInt(minOffset, maxOffset + 1);
        return LocalDate.now().plusDays(randomDays);
    }

    public static LocalDate generateFutureDate(int fromDays, int toDays) {
        return generateRandomDate(fromDays, toDays);
    }

    public static LocalDate generateFutureDate(int offsetDays) {
        return LocalDate.now().plusDays(offsetDays);
    }

    public static LocalDate generatePastDate(int fromDays, int toDays) {
        return generateRandomDate(-toDays, -fromDays);
    }

    public static BookingDates generateRandomDates() {
        var checkIn = DateUtils.generateFutureDate(1, 365);
        var checkOut = checkIn.plusDays(1);
        return new BookingDates(checkIn.toString(), checkOut.toString());
    }
}
