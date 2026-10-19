import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login login = new Login();

        System.out.println("        USER REGISTRATION");

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
            System.out.println("           USER LOGIN");
            System.out.println("=====================================");

            System.out.print("Enter username: ");
            String loginUsername = scanner.nextLine();

            System.out.print("Enter password: ");
            String loginPassword = scanner.nextLine();

            login.loginUser(loginUsername, loginPassword);
            String loginStatus = login.returnLoginStatus();
            System.out.println();
            System.out.println(loginStatus);

            // Only move into QuickChat if login actually succeeded
            if (loginStatus.toLowerCase().contains("successful")) {
                runQuickChat(scanner);
            }
        }

        scanner.close();
    }

    // ===== Part 2: QuickChat messaging menu =====
    private static void runQuickChat(Scanner scanner) {
        System.out.println();
        System.out.println("Welcome to QuickChat.");

        System.out.print("How many messages would you like to enter? ");
        int totalToEnter = Integer.parseInt(scanner.nextLine());
        int messagesEntered = 0;
        int messageCounter = 0;

        boolean running = true;
        while (running) {
            System.out.println("\n1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    if (messagesEntered >= totalToEnter) {
                        System.out.println("You have reached your message limit.");
                        break;
                    }

                    System.out.print("Recipient: ");
                    String recipient = scanner.nextLine();

                    System.out.print("Message: ");
                    String text = scanner.nextLine();

                    if (text.length() > 250) {
                        System.out.println("Please enter a message of less than 250 characters.");
                        break;
                    }

                    Message msg = new Message(recipient, text, messageCounter);
                    messageCounter++;

                    System.out.println("\n0) Send Message");
                    System.out.println("1) Disregard Message");
                    System.out.println("2) Store Message to send later");
                    System.out.print("Choose an option: ");
                    String action = scanner.nextLine();

                    switch (action) {
                        case "0":
                            msg.send();
                            System.out.println("Message successfully sent");
                            msg.printFullDetails();
                            break;
                        case "1":
                            msg.disregard();
                            System.out.println("Press 0 to delete the message");
                            break;
                        case "2":
                            msg.store();
                            System.out.println("Message successfully stored");
                            break;
                    }

                    messagesEntered++;
                    break;

                case "2":
                    System.out.println("Coming Soon.");
                    break;

                case "3":
                    running = false;
                    System.out.println("Total messages sent: " + Message.getTotalMessagesSent());
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
