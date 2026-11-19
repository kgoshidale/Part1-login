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

/**
 * Part 3: holds the arrays (populated at run time, nothing hard-coded) and
 * the features of the "Stored Messages" menu.
 */
public class MessageManager {

    private static final List<Message> sentMessages = new ArrayList<>();
    private static final List<Message> disregardedMessages = new ArrayList<>();
    private static final List<Message> storedMessages = new ArrayList<>();
    private static final List<String> messageHashes = new ArrayList<>();
    private static final List<String> messageIDs = new ArrayList<>();

    private MessageManager() { }

    // ---------- Populating the arrays ----------

    public static void addSent(Message m) {
        sentMessages.add(m);
        index(m);
    }

    public static void addDisregarded(Message m) {
        disregardedMessages.add(m);
        index(m);
    }

    public static void addStored(Message m) {
        storedMessages.add(m);
        index(m);
    }

    private static void index(Message m) {
        if (!messageIDs.contains(m.getMessageID())) {
            messageIDs.add(m.getMessageID());
            messageHashes.add(m.getMessageHash());
        }
    }

    /** Reads the JSON file written by Message.storeMessage() into the stored messages array. */
    public static void loadStoredMessages() {
        for (Message m : storedMessages) {
            removeIndex(m);
        }
        storedMessages.clear();

        File file = new File(Message.getStorageFile());
        if (!file.exists() || file.length() == 0) {
            return;
        }
        Type listType = new TypeToken<List<Message>>() {}.getType();
        try (FileReader reader = new FileReader(file)) {
            List<Message> loaded = new Gson().fromJson(reader, listType);
            if (loaded != null) {
                for (Message m : loaded) {
                    addStored(m);
                }
            }
        } catch (IOException | RuntimeException e) {
            System.out.println("Could not read stored messages: " + e.getMessage());
        }
    }

    // ---------- Menu features ----------

    /** a. Sender and recipient of all stored messages. */
    public static String displaySenderAndRecipient(String sender) {
        if (storedMessages.isEmpty()) {
            return "There are no stored messages.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message m : storedMessages) {
            sb.append("Sender: ").append(sender)
                    .append(" | Recipient: ").append(m.getRecipient())
                    .append(System.lineSeparator());
        }
        return sb.toString().trim();
    }

    /** b. Longest stored message (text only). */
    public static String getLongestStoredMessage() {
        if (storedMessages.isEmpty()) {
            return "There are no stored messages.";
        }
        Message longest = storedMessages.get(0);
        for (Message m : storedMessages) {
            if (m.getMessageText().length() > longest.getMessageText().length()) {
                longest = m;
            }
        }
        return longest.getMessageText();
    }

    /** c. Finds a message by ID across the sent, stored and disregarded arrays. */
    public static Message findByMessageID(String id) {
        for (Message m : allMessages()) {
            if (m.getMessageID().equals(id)) {
                return m;
            }
        }
        return null;
    }

    /** c. Returns the message text for an ID (what the unit test checks). */
    public static String searchByMessageID(String id) {
        Message m = findByMessageID(id);
        return m == null ? "No message found with that ID." : m.getMessageText();
    }

    /** c. Recipient and message for display in the menu. */
    public static String displayByMessageID(String id) {
        Message m = findByMessageID(id);
        return m == null ? "No message found with that ID."
                : "Recipient: " + m.getRecipient() + System.lineSeparator()
                + "Message: " + m.getMessageText();
    }

    /** d. All sent or stored messages for a recipient. */
    public static List<String> searchByRecipient(String recipient) {
        List<String> found = new ArrayList<>();
        for (Message m : storedMessages) {
            if (m.getRecipient().equals(recipient)) {
                found.add(m.getMessageText());
            }
        }
        for (Message m : sentMessages) {
            if (m.getRecipient().equals(recipient)) {
                found.add(m.getMessageText());
            }
        }
        return found;
    }

    /** e. Deletes a message using its hash (also updates the JSON file if it was stored). */
    public static String deleteByHash(String hash) {
        Message target = null;
        for (Message m : allMessages()) {
            if (m.getMessageHash().equalsIgnoreCase(hash)) {
                target = m;
                break;
            }
        }
        if (target == null) {
            return "No message found with that hash.";
        }
        boolean wasStored = storedMessages.remove(target);
        sentMessages.remove(target);
        disregardedMessages.remove(target);
        removeIndex(target);
        if (wasStored) {
            rewriteJson();
        }
        String text = target.getMessageText();
        if (text.endsWith(".")) {
            text = text.substring(0, text.length() - 1);   // matches the wording in the brief
        }
        return "Message \"" + text + "\" successfully deleted.";
    }

    /** f. Report of all sent messages: hash, recipient, message (as per the unit test). */
    public static String displayReport() {
        if (sentMessages.isEmpty()) {
            return "No sent messages to report.";
        }
        StringBuilder sb = new StringBuilder();
        int count = 1;
        for (Message m : sentMessages) {
            sb.append("Message ").append(count++).append(System.lineSeparator())
                    .append("Message Hash: ").append(m.getMessageHash()).append(System.lineSeparator())
                    .append("Recipient: ").append(m.getRecipient()).append(System.lineSeparator())
                    .append("Message: ").append(m.getMessageText()).append(System.lineSeparator())
                    .append(System.lineSeparator());
        }
        return sb.toString().trim();
    }

    /** f. Full details of every stored message. */
    public static String displayStoredReport() {
        if (storedMessages.isEmpty()) {
            return "There are no stored messages.";
        }
        StringBuilder sb = new StringBuilder();
        int count = 1;
        for (Message m : storedMessages) {
            sb.append("Stored message ").append(count++).append(System.lineSeparator())
                    .append(m.getDetails()).append(System.lineSeparator())
                    .append(System.lineSeparator());
        }
        return sb.toString().trim();
    }

    // ---------- Helpers ----------

    private static List<Message> allMessages() {
        List<Message> all = new ArrayList<>(sentMessages);
        all.addAll(storedMessages);
        all.addAll(disregardedMessages);
        return all;
    }

    private static void removeIndex(Message m) {
        int i = messageIDs.indexOf(m.getMessageID());
        if (i >= 0) {
            messageIDs.remove(i);
            messageHashes.remove(i);
        }
    }

    private static void rewriteJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type listType = new TypeToken<List<Message>>() {}.getType();
        try (FileWriter writer = new FileWriter(Message.getStorageFile())) {
            gson.toJson(storedMessages, listType, writer);
        } catch (IOException e) {
            System.out.println("Could not update JSON file: " + e.getMessage());
        }
    }

    // ---------- Getters (used by tests and Main) ----------

    public static List<Message> getSentMessages() { return sentMessages; }
    public static List<Message> getDisregardedMessages() { return disregardedMessages; }
    public static List<Message> getStoredMessages() { return storedMessages; }
    public static List<String> getMessageHashes() { return messageHashes; }
    public static List<String> getMessageIDs() { return messageIDs; }

    public static List<String> getSentMessageTexts() {
        List<String> texts = new ArrayList<>();
        for (Message m : sentMessages) {
            texts.add(m.getMessageText());
        }
        return texts;
    }

    public static void reset() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
    }
}
