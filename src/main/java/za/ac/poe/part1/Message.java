package za.ac.poe.part1;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Message {

    private static final int MAX_MESSAGE_LENGTH = 250;

    private static int totalMessagesSent = 0;
    private static String storageFile = "messages.json";

    private final String messageID;
    private final int messageNumber;
    private final String recipient;
    private final String messageText;
    private final String messageHash;

    /** Normal use: the message ID is randomly generated. */
    public Message(int messageNumber, String recipient, String messageText) {
        this(generateMessageID(), messageNumber, recipient, messageText);
    }

    /** Lets unit tests supply a known ID (e.g. "0012345678") so the hash can be asserted. */
    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    // ---------- Required methods ----------

    /** Random ten-digit number (leading zeros allowed, e.g. 0012345678). */
    public static String generateMessageID() {
        long id = ThreadLocalRandom.current().nextLong(10_000_000_000L);
        return String.format("%010d", id);
    }

    /** Message ID must be digits only and no more than ten characters. */
    public boolean checkMessageID() {
        return messageID != null && messageID.matches("\\d{1,10}");
    }

    /**
     * Recipient must start with an international code (+), contain digits only
     * and be a realistic length (country code + up to ten digits).
     */
    public String checkRecipientCell() {
        if (recipient != null && recipient.matches("^\\+[1-9]\\d{9,12}$")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. "
                + "Please correct the number and try again.";
    }

    /** Checks the 250 character limit. */
    public String checkMessageLength() {
        int length = messageText == null ? 0 : messageText.length();
        if (length <= MAX_MESSAGE_LENGTH) {
            return "Message ready to send.";
        }
        return "Message exceeds 250 characters by " + (length - MAX_MESSAGE_LENGTH)
                + ", please reduce the size.";
    }

    /** First two digits of ID : message number : FIRSTWORD + LASTWORD (all caps, punctuation removed). */
    public String createMessageHash() {
        String[] words = messageText.trim().split("\\s+");
        String first = cleanWord(words[0]);
        String last = cleanWord(words[words.length - 1]);
        String idPrefix = messageID.length() >= 2 ? messageID.substring(0, 2) : messageID;
        return (idPrefix + ":" + messageNumber + ":" + first + last).toUpperCase();
    }

    /**
     * 1 = send, 2 = disregard, 3 = store.
     * The menu/prompt is handled in Main so this method can be unit tested.
     */
    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                MessageManager.addSent(this);
                totalMessagesSent++;
                return "Message successfully sent.";
            case 2:
                MessageManager.addDisregarded(this);
                return "Press 0 to delete the message.";
            case 3:
                if (storeMessage()) {
                    MessageManager.addStored(this);
                    return "Message successfully stored.";
                }
                return "Message could not be stored.";
            default:
                return "Invalid option.";
        }
    }

    /** Returns every message sent while the program is running. */
    public static String printMessages() {
        List<Message> sent = MessageManager.getSentMessages();
        if (sent.isEmpty()) {
            return "No messages have been sent.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message m : sent) {
            sb.append(m.getDetails()).append(System.lineSeparator())
                    .append(System.lineSeparator());
        }
        return sb.toString().trim();
    }

    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    /** Appends this message to the JSON file (creates the file if needed). */
    public boolean storeMessage() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type listType = new TypeToken<List<Message>>() {}.getType();
        List<Message> stored = new ArrayList<>();

        File file = new File(storageFile);
        if (file.exists() && file.length() > 0) {
            try (FileReader reader = new FileReader(file)) {
                List<Message> existing = gson.fromJson(reader, listType);
                if (existing != null) {
                    stored = existing;
                }
            } catch (IOException | RuntimeException e) {
                System.out.println("Could not read existing JSON file: " + e.getMessage());
                return false;
            }
        }
        stored.add(this);

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(stored, listType, writer);
            return true;
        } catch (IOException e) {
            System.out.println("Could not write JSON file: " + e.getMessage());
            return false;
        }
    }

    // ---------- Helpers ----------

    /** Display order required by the brief: ID, Hash, Recipient, Message. */
    public String getDetails() {
        return "Message ID: " + messageID + System.lineSeparator()
                + "Message Hash: " + messageHash + System.lineSeparator()
                + "Recipient: " + recipient + System.lineSeparator()
                + "Message: " + messageText;
    }

    private static String cleanWord(String word) {
        return word.replaceAll("[^A-Za-z0-9]", "");
    }

    public static void setStorageFile(String path) {
        storageFile = path;
    }

    public static String getStorageFile() {
        return storageFile;
    }

    /** Clears static state - used by the unit tests. */
    public static void resetState() {
        MessageManager.reset();
        totalMessagesSent = 0;
    }

    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public int getMessageNumber() { return messageNumber; }
}
