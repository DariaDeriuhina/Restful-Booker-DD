package testdata;

import org.testng.annotations.DataProvider;
import utils.constants.TestConstants;

import java.util.List;

public class ContactTestData {

    @DataProvider(name = "contactFormData")
    public Object[][] contactFormData() {
        return new Object[][] {
                {
                        TestConstants.DEFAULT_FIRSTNAME,
                        TestConstants.TEST_EMAIL,
                        TestConstants.TEST_PHONE,
                        TestConstants.CONTACT_SUBJECT,
                        TestConstants.CONTACT_MESSAGE,
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
                        TestConstants.DEFAULT_FIRSTNAME,
                        "invalid-email",
                        TestConstants.TEST_PHONE,
                        TestConstants.CONTACT_SUBJECT,
                        "Valid message with enough length.",
                        List.of("must be a well-formed email address")
                },
                {
                        "",
                        TestConstants.TEST_EMAIL,
                        TestConstants.TEST_PHONE,
                        "Subject",
                        "Valid long  message.",
                        List.of("Name may not be blank")
                },
                {
                        TestConstants.DEFAULT_FIRSTNAME,
                        "",
                        "380123456789",
                        "Subject",
                        "Valid long  message.",
                        List.of("Email may not be blank")
                },
                {
                        TestConstants.DEFAULT_FIRSTNAME,
                        "anna@example.com",
                        "",
                        "Subject",
                        "Valid long  message.",
                        List.of("Phone may not be blank", "Phone must be between 11 and 21 characters.")
                },
                {
                        TestConstants.DEFAULT_FIRSTNAME,
                        "anna@example.com",
                        "123",
                        "Subject",
                        "Valid long  message.",
                        List.of("Phone must be between 11 and 21 characters.")
                },
                {
                        TestConstants.DEFAULT_FIRSTNAME,
                        TestConstants.TEST_EMAIL,
                        TestConstants.TEST_PHONE,
                        "",
                        "Valid long  message.",
                        List.of("Subject may not be blank", "Subject must be between 5 and 100 characters.")
                },
                {
                        TestConstants.DEFAULT_FIRSTNAME,
                        TestConstants.TEST_EMAIL,
                        TestConstants.TEST_PHONE,
                        "Hi",
                        "Valid long  message.",
                        List.of("Subject must be between 5 and 100 characters.")
                },
                {
                        TestConstants.DEFAULT_FIRSTNAME,
                        TestConstants.TEST_EMAIL,
                        TestConstants.TEST_PHONE,
                        "Subject",
                        "",
                        List.of("Message may not be blank", "Message must be between 20 and 2000 characters.")
                },
                {
                        TestConstants.DEFAULT_FIRSTNAME,
                        TestConstants.TEST_EMAIL,
                        TestConstants.TEST_PHONE,
                        "Subject",
                        "Too short",
                        List.of("Message must be between 20 and 2000 characters.")
                }
        };
    }
}