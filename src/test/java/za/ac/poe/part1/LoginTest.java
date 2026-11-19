package za.ac.poe.part1;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginTest {

    private Login login;

    @BeforeEach
    void setUp() {
        login = new Login();
    }

    // ---- Username ----
    @Test
    void usernameCorrectlyFormatted() {
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    void usernameIncorrectlyFormattedNoUnderscore() {
        assertFalse(login.checkUserName("kyle1"));
    }

    @Test
    void usernameIncorrectlyFormattedTooLong() {
        assertFalse(login.checkUserName("kyle_smith"));
    }

    // ---- Password complexity ----
    @Test
    void passwordMeetsComplexity() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    void passwordTooShort() {
        assertFalse(login.checkPasswordComplexity("Ab1!"));
    }

    @Test
    void passwordNoCapital() {
        assertFalse(login.checkPasswordComplexity("password1!"));
    }

    @Test
    void passwordNoNumber() {
        assertFalse(login.checkPasswordComplexity("Password!!"));
    }

    @Test
    void passwordNoSpecialCharacter() {
        assertFalse(login.checkPasswordComplexity("Password12"));
    }

    // ---- Cell phone number ----
    @Test
    void cellNumberCorrectlyFormatted() {
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    void cellNumberMissingInternationalCode() {
        assertFalse(login.checkCellPhoneNumber("0838968976"));
    }

    @Test
    void cellNumberContainsLetters() {
        assertFalse(login.checkCellPhoneNumber("+2783abc8976"));
    }

    // ---- Registration messages ----
    @Test
    void registerUserSuccess() {
        assertEquals("Username successfully captured. Password successfully captured. "
                        + "Cell phone number successfully added. User registered successfully.",
                login.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976"));
    }

    @Test
    void registerUserBadUsername() {
        assertEquals("Username is not correctly formatted, please ensure that your username "
                        + "contains an underscore and is no more than five characters in length.",
                login.registerUser("Kyle", "Smith", "kyle!!!!!!", "Ch&&sec@ke99!", "+27838968976"));
    }

    @Test
    void registerUserBadPassword() {
        assertEquals("Password is not correctly formatted, please ensure that the password "
                        + "contains at least eight characters, a capital letter, a number, and a special character.",
                login.registerUser("Kyle", "Smith", "kyl_1", "password", "+27838968976"));
    }

    @Test
    void registerUserBadCellNumber() {
        assertEquals("Cell phone number incorrectly formatted or does not contain international code.",
                login.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "08966553"));
    }

    // ---- Login ----
    @Test
    void loginSuccessful() {
        login.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
        assertEquals("Welcome Kyle Smith, it is great to see you again.",
                login.returnLoginStatus(true));
    }

    @Test
    void loginFailedWrongPassword() {
        login.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.loginUser("kyl_1", "wrongPass1!"));
        assertEquals("Username or password incorrect, please try again.",
                login.returnLoginStatus(false));
    }

    @Test
    void loginFailedBeforeRegistering() {
        assertFalse(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }
}
