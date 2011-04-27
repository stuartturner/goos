package ca.jbrains.auction.test;

import static org.junit.Assert.*;

import org.jivesoftware.smack.packet.Message;
import org.junit.*;

import ca.jbrains.auction.Main;

public class ClassifySniperBidAuctionMessageTest {
    @Test
    public void happyPath() throws Exception {
        assertTrue(Main
                .isSniperBidMessage(messageWithText("SOLVersion 1.1; Command: Bid; Price: 1098")));
    }

    @Test
    public void commandIsNotBid() throws Exception {
        assertFalse(Main
                .isSniperBidMessage(messageWithText("SOLVersion 1.1; Command: XXX"
                        + irrelevantDetails())));
    }

    @Test
    public void messageContents() throws Exception {
        assertEquals("SOLVersion 1.1; Command: Bid; Price: 1098",
                messageWithText("SOLVersion 1.1; Command: Bid; Price: 1098")
                        .getBody());
        assertEquals("not the body", new Message("not the body").getTo());
        assertNull(new Message("not the body").getBody());
    }

    @Test
    public void notSolMessage() throws Exception {
        assertFalse(Main
                .isSniperBidMessage(messageWithText("jbrains 3.0, bitchez; Command: Bid")));
    }

    @Test
    public void messageWithNoBody() throws Exception {
        assertFalse(Main.isSniperBidMessage(new Message()));
    }

    private static Message messageWithText(String text) {
        final Message message = new Message();
        message.setBody(text);
        return message;
    }

    private static String irrelevantDetails() {
        return "; Price: 1098";
    }
}
