package testdata;


import org.testng.annotations.DataProvider;

public class AuthTestData {

    @DataProvider(name = "invalidAuthCases")
    public static Object[][] invalidAuthCases() {
        return new Object[][]{
                {"wrong_user", "password", "Invalid username"},
                {"admin", "wrong_pass", "Invalid password"},
                {"", "", "Empty credentials"},
                {null, null, "Null credentials"}
        };
    }

    @DataProvider(name = "tokenValidationCases")
    public static Object[][] tokenValidationCases() {
        return new Object[][]{
                {"valid", 200, "valid token"},
                {"invalid-token-12345", 403, "invalid token"},
                {"", 401, "empty token"}
        };
    }
}
