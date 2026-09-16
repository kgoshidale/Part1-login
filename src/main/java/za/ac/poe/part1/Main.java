package za.ac.poe.part1;

import java.util.Scanner;

/**
 * Main.java
 *
 * Console entry point for Part 1 - Registration and login feature.
 * No GUI / JOptionPane is used, per the assignment requirements.
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login login = new Login();

        System.out.println("=====================================");
        System.out.println("        USER REGISTRATION");
        System.out.println("=====================================");

        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter username (must contain '_' and be no more than 5 characters): ");
        String username = scanner.nextLine();

        System.out.print("Enter password (min 8 characters, 1 capital, 1 number, 1 special character): ");
        String password = scanner.nextLine();

        System.out.print("Enter South African cell phone number (e.g. +27831234567): ");
        String cellPhoneNumber = scanner.nextLine();

        String registrationResult = login.registerUser(firstName, lastName, username, password, cellPhoneNumber);
        System.out.println();
        System.out.println(registrationResult);

        if (registrationResult.contains("registered successfully")) {
            System.out.println();
            System.out.println("=====================================");
            System.out.println("           USER LOGIN");
            System.out.println("=====================================");

            System.out.print("Enter username: ");
            String loginUsername = scanner.nextLine();

            System.out.print("Enter password: ");
            String loginPassword = scanner.nextLine();

            login.loginUser(loginUsername, loginPassword);
            System.out.println();
            System.out.println(login.returnLoginStatus());
        }

        scanner.close();
    }
}
