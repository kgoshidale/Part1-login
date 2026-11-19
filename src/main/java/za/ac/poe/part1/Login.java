package za.ac.poe.part1;


// Handles user registration and login, including the validation rules
public class Login {

    private String username;
    private String password;
    private String cellNumber;
    private String firstName;
    private String lastName;

    // Username must contain an underscore and be at most five characters
    public boolean checkUserName(String username) {
        return username != null && username.contains("_") && username.length() <= 5;
    }

    // Password needs 8+ characters, a capital letter, a number and a special character
    public boolean checkPasswordComplexity(String password) {
        return password != null
                && password.length() >= 8
                && password.matches(".*[A-Z].*")
                && password.matches(".*\\d.*")
                && password.matches(".*[^A-Za-z0-9].*");
    }

    // Cell number must start with + and an international code
    public boolean checkCellPhoneNumber(String cell) {
        return cell != null && cell.matches("^\\+\\d{1,3}\\d{1,10}$");
    }

    // Validates every field and saves the user if all are correct
    public String registerUser(String firstName, String lastName, String username,
                               String password, String cell) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted, please ensure that your username "
                    + "contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted, please ensure that the password "
                    + "contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber(cell)) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.cellNumber = cell;
        return "Username successfully captured. Password successfully captured. "
                + "Cell phone number successfully added. User registered successfully.";
    }

    // Returns first name and last name together
    public String getFullName() {
        return firstName + " " + lastName;
    }

    // Checks the login details against the registered user
    public boolean loginUser(String username, String password) {
        return this.username != null
                && this.username.equals(username)
                && this.password.equals(password);
    }

    // Welcome message for a successful login, error message otherwise
    public String returnLoginStatus(boolean loggedIn) {
        return loggedIn
                ? "Welcome " + firstName + " " + lastName + ", it is great to see you again."
                : "Username or password incorrect, please try again.";
    }
}