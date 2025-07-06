package api.testdata;

import api.models.BookingDates;
import api.models.BookingRequest;

public class BookingRequestBuilder {
    private int roomid;
    private String firstname;
    private String lastname;
    private boolean depositpaid = false;
    private String email;
    private String phone;
    private String checkin;
    private String checkout;

    private BookingRequestBuilder() {}

    public static BookingRequestBuilder newRequest() {
        return new BookingRequestBuilder();
    }

    public BookingRequestBuilder forRoom(int roomid) {
        this.roomid = roomid;
        return this;
    }

    public BookingRequestBuilder by(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
        return this;
    }

    public BookingRequestBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public BookingRequestBuilder withPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public BookingRequestBuilder withDeposit(boolean deposit) {
        this.depositpaid = deposit;
        return this;
    }

    public BookingRequestBuilder from(String checkin) {
        this.checkin = checkin;
        return this;
    }

    public BookingRequestBuilder to(String checkout) {
        this.checkout = checkout;
        return this;
    }

    public BookingRequestBuilder withoutDates() {
        this.checkin = null;
        this.checkout = null;
        return this;
    }

    public BookingRequest build() {
        BookingDates bookingDates = (checkin != null && checkout != null)
                ? new BookingDates(checkin, checkout)
                : null;

        return new BookingRequest(
                roomid,
                firstname,
                lastname,
                depositpaid,
                email,
                phone,
                bookingDates
        );
    }
}