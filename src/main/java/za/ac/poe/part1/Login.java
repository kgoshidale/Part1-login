package za.ac.poe.part1;

public class Login {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String cellPhoneNumber;

    private boolean loggedIn = false;

    public Login() {
    }


    public boolean checkUserName(String username) {
        if (username == null) {
            return false;
        }
        return username.length() <= 5 && username.contains("_");
    }


    public boolean checkPasswordComplexity(String password) {
        if (password == null) {
            return false;
        }
        String regex = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()\\-_=+{}\\[\\]|:;\"'<>,.?/~`]).{8,}$";
        return password.matches(regex);
    }

    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        if (cellPhoneNumber == null) {
            return false;
        }
        String regex = "^\\+27[0-9]{1,10}$";
        return cellPhoneNumber.matches(regex);
    }

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

    public boolean loginUser(String enteredUsername, String enteredPassword) {
        loggedIn = enteredUsername != null
                && enteredPassword != null
                && enteredUsername.equals(this.username)
                && enteredPassword.equals(this.password);
        return loggedIn;
    }

    public String returnLoginStatus() {
        if (loggedIn) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }


}
