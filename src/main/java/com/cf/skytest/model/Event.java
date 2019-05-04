package com.cf.skytest.model;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.BooleanUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Data
@Builder
public class Event {

    public static Event fromInputFeed(List<String> inputFeed) {
        long startTimeMs = Long.parseLong(inputFeed.get(4));
        return Event.builder().id(inputFeed.get(0))
                .category(inputFeed.get(1))
                .subCategory(inputFeed.get(2))
                .name(inputFeed.get(3))
                .startTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(startTimeMs), ZoneId.systemDefault()))
                .displayed(BooleanUtils.toBoolean(Integer.parseInt(inputFeed.get(5))))
                .suspended(BooleanUtils.toBoolean(Integer.parseInt(inputFeed.get(6))))
                .build();
    }

    String id;
    String category;
    String subCategory;
    String name;
    LocalDateTime startTime;
    boolean displayed;
    boolean suspended;
}
