package za.ac.poe.part1;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// Holds the Part 3 arrays and the search, delete and report features
public class MessageStore {

    private static final String NL = System.lineSeparator();
    private static final String NO_STORED = "No stored messages are available.";

    private final List<Message> sentMessages = new ArrayList<>();
    private final List<Message> disregardedMessages = new ArrayList<>();
    private final List<Message> storedMessages = new ArrayList<>();
    private final List<String> messageIDs = new ArrayList<>();
    private final List<String> messageHashes = new ArrayList<>();

    // Adds a message to the array that matches the chosen action (0 sent, 1 disregarded, 2 stored)
    public void record(Message message, String action) {
        switch (action) {
            case "0":
                sentMessages.add(message);
                break;
            case "1":
                disregardedMessages.add(message);
                break;
            case "2":
                storedMessages.add(message);
                break;
            default:
                return;
        }
        messageIDs.add(message.getMessageID());
        messageHashes.add(message.getMessageHash());
    }

    // Adapted from the Gson User Guide (Google, 2023) – https://github.com/google/gson
    // Reads the stored messages back from the JSON file
    public void loadStoredMessages() {
        try {
            java.nio.file.Path path = Paths.get(Message.getStorageFile());
            if (!Files.exists(path)) {
                return;
            }
            String json = Files.readString(path);
            if (json.isBlank()) {
                return;
            }
            for (JsonElement element : JsonParser.parseString(json).getAsJsonArray()) {
                JsonObject entry = element.getAsJsonObject();
                String id = entry.get("messageID").getAsString();
                if (!messageIDs.contains(id)) {
                    record(new Message(id,
                            entry.get("messageHash").getAsString(),
                            entry.get("recipient").getAsString(),
                            entry.get("message").getAsString()), "2");
                }
            }
        } catch (IOException | RuntimeException e) {
            System.out.println("Could not read stored messages: " + e.getMessage());
        }
    }

    private void saveStoredMessages() {
        JsonArray array = new JsonArray();
        for (Message message : storedMessages) {
            array.add(message.toJson());
        }
        try {
            Files.writeString(Paths.get(Message.getStorageFile()),
                    new GsonBuilder().setPrettyPrinting().create().toJson(array));
        } catch (IOException e) {
            System.out.println("Could not update the stored messages file: " + e.getMessage());
        }
    }

    private List<Message> allMessages() {
        List<Message> all = new ArrayList<>(sentMessages);
        all.addAll(disregardedMessages);
        all.addAll(storedMessages);
        return all;
    }

    public String senderAndRecipientReport(String sender) {
        if (storedMessages.isEmpty()) {
            return NO_STORED;
        }
        StringBuilder report = new StringBuilder();
        for (Message message : storedMessages) {
            report.append("Sender: ").append(sender)
                    .append(" | Recipient: ").append(message.getRecipient()).append(NL);
        }
        return report.toString().trim();
    }

    public String longestStoredMessage() {
        if (storedMessages.isEmpty()) {
            return NO_STORED;
        }
        Message longest = storedMessages.get(0);
        for (Message message : storedMessages) {
            if (message.getMessageText().length() > longest.getMessageText().length()) {
                longest = message;
            }
        }
        return longest.getMessageText();
    }

    // Looks through the sent, disregarded and stored arrays
    public Message findByMessageID(String id) {
        for (Message message : allMessages()) {
            if (message.getMessageID().equals(id)) {
                return message;
            }
        }
        return null;
    }

    public String searchByMessageID(String id) {
        Message found = findByMessageID(id);
        if (found == null) {
            return "No message found.";
        }
        return "Recipient: " + found.getRecipient() + NL + "Message: " + found.getMessageText();
    }

    // Returns the text of every message sent to this recipient
    public List<String> searchByRecipient(String recipient) {
        List<String> results = new ArrayList<>();
        for (Message message : allMessages()) {
            if (message.getRecipient().equals(recipient)) {
                results.add(message.getMessageText());
            }
        }
        return results;
    }

    public String deleteByHash(String hash) {
        for (List<Message> group : List.of(sentMessages, disregardedMessages, storedMessages)) {
            for (int i = 0; i < group.size(); i++) {
                Message message = group.get(i);
                if (message.getMessageHash().equalsIgnoreCase(hash.trim())) {
                    group.remove(i);
                    messageIDs.remove(message.getMessageID());
                    messageHashes.remove(message.getMessageHash());
                    if (group == storedMessages) {
                        saveStoredMessages();
                    }
                    String text = message.getMessageText();
                    if (text.endsWith(".")) {
                        text = text.substring(0, text.length() - 1);
                    }
                    return "Message \"" + text + "\" successfully deleted.";
                }
            }
        }
        return "No message found with that hash.";
    }

    // Sent report: hash, recipient and message for every sent message
    public String sentReport() {
        if (sentMessages.isEmpty()) {
            return "No sent messages are available.";
        }
        StringBuilder report = new StringBuilder();
        for (Message message : sentMessages) {
            report.append("Message Hash: ").append(message.getMessageHash()).append(NL)
                    .append("Recipient: ").append(message.getRecipient()).append(NL)
                    .append("Message: ").append(message.getMessageText()).append(NL).append(NL);
        }
        return report.toString().trim();
    }

    // Stored report: full details of every stored message
    public String storedReport() {
        if (storedMessages.isEmpty()) {
            return NO_STORED;
        }
        StringBuilder report = new StringBuilder();
        for (Message message : storedMessages) {
            report.append(message).append(NL).append(NL);
        }
        return report.toString().trim();
    }

    public List<String> getSentMessageTexts() {
        List<String> texts = new ArrayList<>();
        for (Message message : sentMessages) {
            texts.add(message.getMessageText());
        }
        return texts;
    }

    public List<String> getMessageIDs() {
        return new ArrayList<>(messageIDs);
    }

    public List<String> getMessageHashes() {
        return new ArrayList<>(messageHashes);
    }
}
