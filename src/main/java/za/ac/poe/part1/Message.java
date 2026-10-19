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

    public Boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    public String checkRecipientCell(String recipient) {
        if (recipient.length() <= 12 && recipient.startsWith("+")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. "
                + "Please correct the number and try again.";
    }

    public String createMessageHash() {
        String idPrefix = messageID.substring(0, 2);
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];
        String hash = idPrefix + ":" + messageNumber + ":" + firstWord + lastWord;
        return hash.toUpperCase();
    }


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


    public static String printMessages() {
        StringBuilder sb = new StringBuilder();
        for (String msg : sentMessages) {
            sb.append(msg).append("\n");
        }
        return sb.toString();
    }


    public static int returnTotalMessages() {
        return totalMessagesSent;
    }


    public void storeMessage() {
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
