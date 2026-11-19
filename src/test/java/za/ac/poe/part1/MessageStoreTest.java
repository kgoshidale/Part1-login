package za.ac.poe.part1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MessageStoreTest {

    private MessageStore store;
    private Message msg1;
    private Message msg2;
    private Message msg3;
    private Message msg4;
    private Message msg5;

    @BeforeEach
    void setUp(@TempDir Path tempDir) {
        Message.setStorageFile(tempDir.resolve("messages.json").toString());
        store = new MessageStore();

        msg1 = new Message("+27834557896", "Did you get the cake?", 0);
        msg2 = new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.", 1);
        msg3 = new Message("+27834484567", "Yohooo, I am at your gate.", 2);
        msg4 = new Message("0838884567", "It is dinner time!", 3);
        msg5 = new Message("+27838884567", "Ok, I am leaving without you.", 4);

        store.record(msg1, "0");
        store.record(msg2, "2");
        store.record(msg3, "1");
        store.record(msg4, "0");
        store.record(msg5, "2");
    }

    @Test
    void sentMessagesArrayIsPopulated() {
        assertEquals(List.of("Did you get the cake?", "It is dinner time!"), store.getSentMessageTexts());
    }

    @Test
    void idAndHashArraysHoldEveryMessage() {
        assertEquals(5, store.getMessageIDs().size());
        assertEquals(5, store.getMessageHashes().size());
    }

    @Test
    void longestStoredMessageIsMessageTwo() {
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                store.longestStoredMessage());
    }

    @Test
    void searchByMessageIdFindsMessageFour() {
        assertEquals("It is dinner time!", store.findByMessageID(msg4.getMessageID()).getMessageText());
        assertTrue(store.searchByMessageID(msg4.getMessageID()).contains("0838884567"));
    }

    @Test
    void searchByUnknownMessageIdFindsNothing() {
        assertEquals("No message found.", store.searchByMessageID("0000000000x"));
    }

    @Test
    void searchByRecipientFindsMessagesTwoAndFive() {
        assertEquals(List.of("Where are you? You are late! I have asked you to be on time.",
                        "Ok, I am leaving without you."),
                store.searchByRecipient("+27838884567"));
    }

    @Test
    void deleteByHashRemovesMessageTwo() {
        assertEquals("Message \"Where are you? You are late! I have asked you to be on time\" "
                        + "successfully deleted.",
                store.deleteByHash(msg2.getMessageHash()));
        assertNull(store.findByMessageID(msg2.getMessageID()));
        assertEquals(4, store.getMessageHashes().size());
    }

    @Test
    void deleteByUnknownHashFindsNothing() {
        assertEquals("No message found with that hash.", store.deleteByHash("00:9:NOTAHASH"));
    }

    @Test
    void sentReportShowsHashRecipientAndMessage() {
        String report = store.sentReport();
        assertTrue(report.contains(msg1.getMessageHash()));
        assertTrue(report.contains("+27834557896"));
        assertTrue(report.contains("Did you get the cake?"));
        assertTrue(report.contains(msg4.getMessageHash()));
        assertTrue(report.contains("It is dinner time!"));
        assertFalse(report.contains("Where are you?"));
    }
}
