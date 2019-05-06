package com.cf.skytest.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Data
@Builder
@ToString
public class StreamPacket {

    public static StreamPacket fromInputFeed(List<String> inputFeed) {
        long timestampMs = Long.parseLong(inputFeed.get(4));
        return StreamPacket.builder()
                .msgId(inputFeed.get(1))
                .operation(inputFeed.get(2))
                .type(inputFeed.get(3))
                .timestamp(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampMs), ZoneId.systemDefault()))
                .bodyElements(inputFeed.subList(5, inputFeed.size()))
                .build();
    }

    String msgId;
    String operation;
    String type;
    LocalDateTime timestamp;
    List<String> bodyElements;

    public boolean isCreate() {
        return StringUtils.equals(operation, "create");
    }
}
