package za.ac.poe.part1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {


    private static final String RECIPIENT_1 = "+27718693002";
    private static final String TEXT_1 = "Hi Mike, can you join us for dinner tonight?";
    private static final String RECIPIENT_2 = "08575975889";
    private static final String TEXT_2 = "Hi Keegan, did you receive the payment?";

    @BeforeEach
    void setUp(@TempDir Path tempDir) {
        Message.resetState();
        Message.setStorageFile(tempDir.resolve("messages.json").toString());
    }

    @Test
    void messageLengthSuccess() {
        Message m = new Message(0, RECIPIENT_1, TEXT_1);
        assertEquals("Message ready to send.", m.checkMessageLength());
    }

    @Test
    void messageLengthFailure() {
        String longText = "a".repeat(260);
        Message m = new Message(0, RECIPIENT_1, longText);
        assertEquals("Message exceeds 250 characters by 10, please reduce the size.", m.checkMessageLength());
    }

    @Test
    void recipientSuccess() {
        Message m = new Message(0, RECIPIENT_1, TEXT_1);
        assertEquals("Cell phone number successfully captured.", m.checkRecipientCell());
    }

    @Test
    void recipientFailureNoInternationalCode() {
        Message m = new Message(1, RECIPIENT_2, TEXT_2);
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. "
                + "Please correct the number and try again.", m.checkRecipientCell());
    }

    @Test
    void recipientFailurePlusOnly() {
        Message m = new Message(0, "+abc", TEXT_1);
        assertNotEquals("Cell phone number successfully captured.", m.checkRecipientCell());
    }

    @Test
    void hashTestCase1() {
        Message m = new Message("0012345678", 0, RECIPIENT_1, TEXT_1);
        assertEquals("00:0:HITONIGHT", m.createMessageHash());
    }

    @Test
    void hashTestCase2() {
        Message m = new Message("0012345678", 1, RECIPIENT_2, TEXT_2);
        assertEquals("00:1:HIPAYMENT", m.createMessageHash());
    }

    @Test
    void messageIdIsCreatedAndValid() {
        Message m = new Message(0, RECIPIENT_1, TEXT_1);
        assertEquals(10, m.getMessageID().length());
        assertTrue(m.checkMessageID());
        assertTrue(("Message ID generated: " + m.getMessageID()).matches("Message ID generated: \\d{10}"));
    }

    @Test
    void messageIdTooLongFails() {
        Message m = new Message("12345678901", 0, RECIPIENT_1, TEXT_1);
        assertFalse(m.checkMessageID());
    }

    @Test
    void userSelectsSend() {
        Message m = new Message(0, RECIPIENT_1, TEXT_1);
        assertEquals("Message successfully sent.", m.sentMessage(1));
    }

    @Test
    void userSelectsDisregard() {
        Message m = new Message(1, RECIPIENT_2, TEXT_2);
        assertEquals("Press 0 to delete the message.", m.sentMessage(2));
    }

    @Test
    void userSelectsStore() throws Exception {
        Path file = Path.of("target", "test-messages.json");
        Files.deleteIfExists(file);
        Message.setStorageFile(file.toString());
        Message m = new Message(0, RECIPIENT_1, TEXT_1);
        assertEquals("Message successfully stored.", m.sentMessage(3));
        assertTrue(Files.readString(file).contains(m.getMessageID()));
    }

    @Test
    void totalMessagesSentOnlyCountsSent() {
        new Message(0, RECIPIENT_1, TEXT_1).sentMessage(1);   // sent
        new Message(1, RECIPIENT_2, TEXT_2).sentMessage(2);   // disregarded
        assertEquals(1, Message.returnTotalMessages());
    }

    @Test
    void printMessagesShowsSentMessages() {
        Message m = new Message("0012345678", 0, RECIPIENT_1, TEXT_1);
        m.sentMessage(1);
        String printed = Message.printMessages();
        assertTrue(printed.contains("00:0:HITONIGHT"));
        assertTrue(printed.contains(TEXT_1));
    }
}
