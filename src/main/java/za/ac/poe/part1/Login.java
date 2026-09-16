package za.ac.poe.part1;

/**
 * Login.java
 *
 * Handles user registration and login for Part 1 of the PoE.
 *
 * Regex validation approach for the cell phone checker was informed by
 * general guidance on Java regular expressions for phone number validation
 * (Baeldung, "Validating Phone Numbers with Java", https://www.baeldung.com/java-validate-phone-numbers).
 * Adjust this reference to match whatever source you actually consulted.
 */
public class Login {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String cellPhoneNumber;

    private boolean loggedIn = false;

    public Login() {
    }

    /**
     * Checks that the username contains an underscore and is no
     * more than five characters long.
     */
    public boolean checkUserName(String username) {
        if (username == null) {
            return false;
        }
        return username.length() <= 5 && username.contains("_");
    }

    /**
     * Checks that the password is at least 8 characters long and
     * contains a capital letter, a number, and a special character.
     */
    public boolean checkPasswordComplexity(String password) {
        if (password == null) {
            return false;
        }
        String regex = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()\\-_=+{}\\[\\]|:;\"'<>,.?/~`]).{8,}$";
        return password.matches(regex);
    }

    /**
     * Checks that the cell phone number contains the South African
     * international country code (+27) followed by the subscriber
     * number, which must be no more than ten characters long.
     */
    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        if (cellPhoneNumber == null) {
            return false;
        }
        String regex = "^\\+27[0-9]{1,10}$";
        return cellPhoneNumber.matches(regex);
    }

    /**
     * Validates and registers a new user, returning the appropriate
     * message for whichever condition applies.
     */
    public String registerUser(String firstName, String lastName, String username,
                                String password, String cellPhoneNumber) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username "
                    + "contains an underscore and is no more than five characters in length.";
        }

        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password "
                    + "contains at least eight characters, a capital letter, a number, "
                    + "and a special character.";
        }

        if (!checkCellPhoneNumber(cellPhoneNumber)) {
            return "Cell number is incorrectly formatted or does not contain an international "
                    + "code; please correct the number and try again.";
        }

        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;

        return "Username successfully captured. Password successfully captured. "
                + "Cell number successfully captured. You have been registered successfully.";
    }

    /**
     * Verifies that the entered username and password match the
     * details captured at registration.
     */
    public boolean loginUser(String enteredUsername, String enteredPassword) {
        loggedIn = enteredUsername != null
                && enteredPassword != null
                && enteredUsername.equals(this.username)
                && enteredPassword.equals(this.password);
        return loggedIn;
    }

    /**
     * Returns the appropriate message depending on the outcome of
     * the last call to loginUser().
     */
    public String returnLoginStatus() {
        if (loggedIn) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }

    // Getters, useful for testing and for the console app.
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getCellPhoneNumber() {
        return cellPhoneNumber;
    }
}
