package com.cf.skytest.model;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

public class StreamPacketTest {

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
        StreamPacket streamPacket = StreamPacket.fromInputFeed(feed);
        Assert.assertEquals(streamPacket.getMsgId(), msgId);
        Assert.assertEquals(streamPacket.getOperation(), operation);
        Assert.assertEquals(streamPacket.getType(), type);
        Assert.assertEquals(streamPacket.getTimestamp(), LocalDateTime.ofInstant(now, ZoneId.systemDefault()));
        Assert.assertThat(streamPacket.getBodyElements(), CoreMatchers.hasItems(body1, body2, body3));
    }

}