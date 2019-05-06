package com.cf.skytest.model;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

public class PacketTest {

    @Test
    public void givenValidInput_whenFromInputFeed_thenCreatStreamPacket() {
        String msgId = "msgId";
        String operation = "operation";
        String type = "type";
        Instant now = Instant.now();
        String timestamp = Long.toString(now.toEpochMilli());
        String body1 = "body1";
        String body2 = "body2";
        String body3 = "body3";
        List<String> feed = Arrays.asList("", msgId, operation, type, timestamp, body1, body2, body3);
        Packet packet = Packet.fromInputFeed(feed);
        Assert.assertEquals(packet.getMsgId(), msgId);
        Assert.assertEquals(packet.getOperation(), operation);
        Assert.assertEquals(packet.getType(), type);
        Assert.assertEquals(packet.getTimestamp(), LocalDateTime.ofInstant(now, ZoneId.systemDefault()));
        Assert.assertThat(packet.getBodyElements(), CoreMatchers.hasItems(body1, body2, body3));
    }

}