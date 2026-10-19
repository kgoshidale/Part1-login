package za.ac.poe.part1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Message {

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String status; // "sent", "disregarded", "stored"

    private static int totalMessagesSent = 0;
    private static List<String> sentMessages = new ArrayList<>();

    public Message(String recipient, String messageText, int messageNumber) {
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageNumber = messageNumber;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
    }

    private String generateMessageID() {
        Random rand = new Random();
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            id.append(rand.nextInt(10));
        }
        return id.toString();
    }

    // Ensures the message ID is not more than ten characters
    public Boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    // Ensures recipient cell number is no more than ten characters and starts with a code (e.g. "+27...")
    public String checkRecipientCell(String recipient) {
        if (recipient.length() <= 12 && recipient.startsWith("+")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. "
                + "Please correct the number and try again.";
    }

    // Builds the hash: first two digits of ID + ":" + message number + ":" + first & last word, all caps
    public String createMessageHash() {
        String idPrefix = messageID.substring(0, 2);
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];
        String hash = idPrefix + ":" + messageNumber + ":" + firstWord + lastWord;
        return hash.toUpperCase();
    }

    // Lets the user choose to send, store, or disregard the message
    public String sentMessage(String choice) {
        switch (choice) {
            case "0":
                this.status = "sent";
                totalMessagesSent++;
                sentMessages.add(toString());
                return "Message successfully sent.";
            case "1":
                this.status = "disregarded";
                return "Press 0 to delete the message.";
            case "2":
                this.status = "stored";
                return "Message successfully stored.";
            default:
                return "Invalid option.";
        }
    }

    // Returns all messages sent while the program is running
    public static String printMessages() {
        StringBuilder sb = new StringBuilder();
        for (String msg : sentMessages) {
            sb.append(msg).append("\n");
        }
        return sb.toString();
    }

    // Returns the total number of messages sent
    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    // Research task: store message details in a JSON file
    public void storeMessage() {
        // Example using org.json or Gson — add the dependency in your pom.xml, e.g.:
        // implementation 'com.google.code.gson:gson:2.10.1' (if using Gradle)
        // or org.json / Gson via Maven if using Maven
        //
        // Simplest approach with org.json (Maven dependency: org.json:json:20231013):
        //
        // JSONObject obj = new JSONObject();
        // obj.put("messageID", messageID);
        // obj.put("messageHash", messageHash);
        // obj.put("recipient", recipient);
        // obj.put("message", messageText);
        //
        // try (FileWriter file = new FileWriter("messages.json", true)) {
        //     file.write(obj.toString());
        //     file.write(System.lineSeparator());
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
    }

    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return "Message ID: " + messageID + "\n"
                + "Message Hash: " + messageHash + "\n"
                + "Recipient: " + recipient + "\n"
                + "Message: " + messageText;
    }
}
