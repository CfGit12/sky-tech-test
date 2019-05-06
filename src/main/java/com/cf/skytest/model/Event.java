package com.cf.skytest.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.BooleanUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Data
@Builder
@ToString
public class Event {

    public static Event valid() {
        return Event.builder()
                .id("1")
                .category("category")
                .subCategory("subCategory")
                .name("name")
                .startTime(LocalDateTime.now())
                .displayed(true)
                .suspended(false)
                .markets(Arrays.asList(Market.valid()))
                .build();
    }

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

    public void addMarket(Market market) {
        if (markets != null) {
            markets.add(market);
        }
        else {
            markets = new ArrayList<>();
            markets.add(market);
        }
    }

    /**
     * Updates this event with the details of the passed in event, excluding the list of markets.
     * @param event
     */
    public void update(Event event) {
        id = event.getId();
        category = event.getCategory();
        subCategory = event.getSubCategory();
        name = event.getName();
        startTime = event.getStartTime();
        displayed = event.isDisplayed();
        suspended = event.isSuspended();
    }

    public void updateMarket(Market market) {
        Optional<Market> opt = markets.stream().filter(m -> m.getId().equals(market.getId())).findFirst();
        if (opt.isPresent()) {
            Market existingMarket = opt.get();
            existingMarket.update(market);
        }

    }

    String id;
    String category;
    String subCategory;
    String name;
    LocalDateTime startTime;
    boolean displayed;
    boolean suspended;

    List<Market> markets;
}
