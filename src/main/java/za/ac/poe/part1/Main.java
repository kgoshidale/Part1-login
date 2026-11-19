package za.ac.poe.part1;


import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Login login = new Login();

        // ---- Part 1: register and log in ----
        System.out.println("=== Registration ===");
        String status;
        do {
            System.out.print("First name: ");
            String first = scanner.nextLine().trim();
            System.out.print("Last name: ");
            String last = scanner.nextLine().trim();
            System.out.print("Username (underscore, max 5 chars): ");
            String user = scanner.nextLine().trim();
            System.out.print("Password: ");
            String pass = scanner.nextLine();
            System.out.print("Cell number (with international code): ");
            String cell = scanner.nextLine().trim();
            status = login.registerUser(first, last, user, pass, cell);
            System.out.println(status);
        } while (!status.contains("registered successfully"));

        System.out.println("\n=== Login ===");
        boolean loggedIn;
        do {
            System.out.print("Username: ");
            String user = scanner.nextLine().trim();
            System.out.print("Password: ");
            String pass = scanner.nextLine();
            loggedIn = login.loginUser(user, pass);
            System.out.println(login.returnLoginStatus(loggedIn));
        } while (!loggedIn);

        // ---- Part 2: QuickChat (only reached after a successful login) ----
        System.out.println("\nWelcome to QuickChat.");
        MessageManager.loadStoredMessages();   // Part 3: read stored messages from the JSON file
        String senderName = login.getFullName();
        int numMessages = readInt("How many messages do you wish to enter? ", 1, Integer.MAX_VALUE);
        int entered = 0;
        boolean running = true;

        while (running) {
            System.out.println("\nPlease choose an option:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            int choice = readInt("Option: ", 1, 4);

            switch (choice) {
                case 1:
                    if (entered >= numMessages) {
                        System.out.println("You have already entered all " + numMessages + " messages.");
                        break;
                    }
                    for (int i = entered; i < numMessages; i++) {
                        System.out.println("\n--- Message " + (i + 1) + " of " + numMessages + " ---");
                        composeMessage(i);
                        entered++;
                    }
                    System.out.println("\nTotal messages sent: " + Message.returnTotalMessages());
                    break;
                case 2:
                    System.out.println("Coming Soon.");
                    break;
                case 3:
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                case 4:
                    storedMessagesMenu(senderName);
                    break;
                default:
                    break;
            }
        }
    }

    private static void storedMessagesMenu(String senderName) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Stored Messages ---");
            System.out.println("1) Display sender and recipient of all stored messages");
            System.out.println("2) Display the longest stored message");
            System.out.println("3) Search for a message ID");
            System.out.println("4) Search for all messages to a recipient");
            System.out.println("5) Delete a message using its hash");
            System.out.println("6) Display report");
            System.out.println("7) Back to main menu");
            int option = readInt("Option: ", 1, 7);

            switch (option) {
                case 1:
                    System.out.println(MessageManager.displaySenderAndRecipient(senderName));
                    break;
                case 2:
                    System.out.println(MessageManager.getLongestStoredMessage());
                    break;
                case 3:
                    System.out.print("Enter the message ID: ");
                    System.out.println(MessageManager.displayByMessageID(scanner.nextLine().trim()));
                    break;
                case 4:
                    System.out.print("Enter the recipient number: ");
                    java.util.List<String> found = MessageManager.searchByRecipient(scanner.nextLine().trim());
                    if (found.isEmpty()) {
                        System.out.println("No messages found for that recipient.");
                    } else {
                        found.forEach(System.out::println);
                    }
                    break;
                case 5:
                    System.out.print("Enter the message hash: ");
                    System.out.println(MessageManager.deleteByHash(scanner.nextLine().trim()));
                    break;
                case 6:
                    System.out.println("=== Sent messages ===");
                    System.out.println(MessageManager.displayReport());
                    System.out.println("\n=== Stored messages ===");
                    System.out.println(MessageManager.displayStoredReport());
                    break;
                default:
                    back = true;
                    break;
            }
        }
    }

    private static void composeMessage(int messageNumber) {
        // Recipient
        String recipient;
        while (true) {
            System.out.print("Recipient cell number (e.g. +27718693002): ");
            recipient = scanner.nextLine().trim();
            String result = new Message(messageNumber, recipient, "x").checkRecipientCell();
            System.out.println(result);
            if (result.equals("Cell phone number successfully captured.")) {
                break;
            }
        }

        // Message text (max 250 characters, not empty)
        String text;
        while (true) {
            System.out.print("Enter your message: ");
            text = scanner.nextLine().trim();
            if (text.isEmpty()) {
                System.out.println("The message cannot be empty.");
            } else if (text.length() > 250) {
                System.out.println("Please enter a message of less than 250 characters.");
            } else {
                break;
            }
        }

        Message message = new Message(messageNumber, recipient, text);
        System.out.println("Message ID generated: " + message.getMessageID());

        System.out.println("\nWhat would you like to do with this message?");
        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message to send later");
        int action = readInt("Option: ", 1, 3);

        String result = message.sentMessage(action);
        System.out.println(result);

        if (action == 1) {
            System.out.println("\n" + message.getDetails());
        } else if (action == 2) {
            System.out.print("> ");
            if (scanner.nextLine().trim().equals("0")) {
                System.out.println("Message deleted.");
            }
        }
    }

    private static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                // fall through to the error message
            }
            System.out.println("Invalid input. Please enter a number"
                    + (max == Integer.MAX_VALUE ? " of at least " + min : " between " + min + " and " + max) + ".");
        }
    }
}
