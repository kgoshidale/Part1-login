package za.ac.poe.part1;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MessageManagerTest {

    private Message m1, m2, m3, m4, m5;

    @BeforeEach
    void setUp(@TempDir Path tempDir) {
        Message.resetState();
        Message.setStorageFile(tempDir.resolve("messages.json").toString());

        m1 = new Message("1011111111", 0, "+27834557896", "Did you get the cake?");
        m2 = new Message("2022222222", 1, "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        m3 = new Message("3033333333", 2, "+27834484567", "Yohooo, I am at your gate.");
        m4 = new Message("4044444444", 3, "0838884567", "It is dinner time!");
        m5 = new Message("5055555555", 4, "+27838884567", "Ok, I am leaving without you.");

        m1.sentMessage(1);   // Sent
        m2.sentMessage(3);   // Stored
        m3.sentMessage(2);   // Disregard
        m4.sentMessage(1);   // Sent
        m5.sentMessage(3);   // Stored
    }

    @Test
    void sentMessagesArrayCorrectlyPopulated() {
        assertEquals(List.of("Did you get the cake?", "It is dinner time!"),
                MessageManager.getSentMessageTexts());
    }

    @Test
    void otherArraysPopulated() {
        assertEquals(1, MessageManager.getDisregardedMessages().size());
        assertEquals(2, MessageManager.getStoredMessages().size());
        assertEquals(5, MessageManager.getMessageIDs().size());
        assertEquals(5, MessageManager.getMessageHashes().size());
    }

    @Test
    void storedMessagesReadFromJson() {
        MessageManager.getStoredMessages().clear();
        MessageManager.loadStoredMessages();
        assertEquals(2, MessageManager.getStoredMessages().size());
        assertEquals("Ok, I am leaving without you.",
                MessageManager.getStoredMessages().get(1).getMessageText());
    }

    @Test
    void displayLongestMessage() {
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                MessageManager.getLongestStoredMessage());
    }

    @Test
    void searchForMessageID() {
        assertEquals("It is dinner time!", MessageManager.searchByMessageID(m4.getMessageID()));
    }

    @Test
    void searchByRecipient() {
        assertEquals(List.of("Where are you? You are late! I have asked you to be on time.",
                        "Ok, I am leaving without you."),
                MessageManager.searchByRecipient("+27838884567"));
    }

    @Test
    void deleteMessageUsingHash() {
        assertEquals("Message \"Where are you? You are late! I have asked you to be on time\" successfully deleted.",
                MessageManager.deleteByHash(m2.getMessageHash()));
        assertEquals(1, MessageManager.getStoredMessages().size());
    }

    @Test
    void deleteUpdatesJsonFile() {
        MessageManager.deleteByHash(m2.getMessageHash());
        MessageManager.loadStoredMessages();
        assertEquals(1, MessageManager.getStoredMessages().size());
    }

    @Test
    void displayReportShowsSentMessages() {
        String report = MessageManager.displayReport();
        assertTrue(report.contains(m1.getMessageHash()));
        assertTrue(report.contains("+27834557896"));
        assertTrue(report.contains("Did you get the cake?"));
        assertTrue(report.contains(m4.getMessageHash()));
        assertTrue(report.contains("It is dinner time!"));
        assertFalse(report.contains("Yohooo"));
    }
}
