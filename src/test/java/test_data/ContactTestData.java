package test_data;

import org.testng.annotations.DataProvider;

import java.util.List;

public class ContactTestData {

    @DataProvider(name = "contactFormData")
    public Object[][] contactFormData() {
        return new Object[][] {
                {
                        "Anna",
                        "anna@example.com",
                        "380123456789",
                        "Booking inquiry",
                        "I would like to book a room at your hotel.",
                        List.of()
                },
                {
                        "",
                        "",
                        "",
                        "",
                        "",
                        List.of(
                                "Name may not be blank",
                                "Email may not be blank",
                                "Phone may not be blank",
                                "Phone must be between 11 and 21 characters.",
                                "Subject may not be blank",
                                "Subject must be between 5 and 100 characters.",
                                "Message may not be blank",
                                "Message must be between 20 and 2000 characters."
                        )
                },
                {
                        "Anna",
                        "invalid-email",
                        "380123456789",
                        "Booking inquiry",
                        "Valid message with enough length.",
                        List.of("must be a well-formed email address")
                },
                {
                        "",
                        "anna@example.com",
                        "380123456789",
                        "Subject",
                        "Valid long  message.",
                        List.of("Name may not be blank")
                },
                {
                        "Anna",
                        "",
                        "380123456789",
                        "Subject",
                        "Valid long  message.",
                        List.of("Email may not be blank")
                },
                {
                        "Anna",
                        "anna@example.com",
                        "",
                        "Subject",
                        "Valid long  message.",
                        List.of("Phone may not be blank", "Phone must be between 11 and 21 characters.")
                },
                {
                        "Anna",
                        "anna@example.com",
                        "123",
                        "Subject",
                        "Valid long  message.",
                        List.of("Phone must be between 11 and 21 characters.")
                },
                {
                        "Anna",
                        "anna@example.com",
                        "380123456789",
                        "",
                        "Valid long  message.",
                        List.of("Subject may not be blank", "Subject must be between 5 and 100 characters.")
                },
                {
                        "Anna",
                        "anna@example.com",
                        "380123456789",
                        "Hi",
                        "Valid long  message.",
                        List.of("Subject must be between 5 and 100 characters.")
                },
                {
                        "Anna",
                        "anna@example.com",
                        "380123456789",
                        "Subject",
                        "",
                        List.of("Message may not be blank", "Message must be between 20 and 2000 characters.")
                },
                {
                        "Anna",
                        "anna@example.com",
                        "380123456789",
                        "Subject",
                        "Too short",
                        List.of("Message must be between 20 and 2000 characters.")
                }
        };
    }
}