package za.ac.poe.part1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * LoginTest.java
 * Unit tests using the exact test data provided in the assignment brief.
 */
class LoginTest {

    private Login login;

    @BeforeEach
    void setUp() {
        login = new Login();
    }

    // ----- Username tests -----

    @Test
    void testUsernameCorrectlyFormatted() {
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    void testUsernameIncorrectlyFormatted() {
        assertFalse(login.checkUserName("kylelllllll"));
    }

    // ----- Password tests -----

    @Test
    void testPasswordMeetsComplexityRequirements() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    void testPasswordDoesNotMeetComplexityRequirements() {
        assertFalse(login.checkPasswordComplexity("password"));
    }

    // ----- Cell phone tests -----

    @Test
    void testCellPhoneNumberCorrectlyFormatted() {
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    void testCellPhoneNumberIncorrectlyFormatted() {
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }

    // ----- registerUser() message tests -----

    @Test
    void testRegisterUserReturnsSuccessMessage() {
        String result = login.registerUser("Kyle", "Naidoo", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(result.contains("registered successfully"));
    }

    @Test
    void testRegisterUserReturnsUsernameErrorMessage() {
        String result = login.registerUser("Kyle", "Naidoo", "kylelllllll", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(result.contains("Username is not correctly formatted"));
    }

    @Test
    void testRegisterUserReturnsPasswordErrorMessage() {
        String result = login.registerUser("Kyle", "Naidoo", "kyl_1", "password", "+27838968976");
        assertTrue(result.contains("Password is not correctly formatted"));
    }

    // ----- Login tests -----

    @Test
    void testLoginSuccessful() {
        login.registerUser("Kyle", "Naidoo", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    void testLoginFailed() {
        login.registerUser("Kyle", "Naidoo", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.loginUser("kyl_1", "wrongPassword"));
    }

    @Test
    void testReturnLoginStatusSuccess() {
        login.registerUser("Kyle", "Naidoo", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        login.loginUser("kyl_1", "Ch&&sec@ke99!");
        assertEquals("Welcome Kyle, Naidoo it is great to see you again.", login.returnLoginStatus());
    }

    @Test
    void testReturnLoginStatusFailure() {
        login.registerUser("Kyle", "Naidoo", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        login.loginUser("kyl_1", "wrongPassword");
        assertEquals("Username or password incorrect, please try again.", login.returnLoginStatus());
    }
}
