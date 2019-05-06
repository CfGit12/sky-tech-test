package com.cf.skytest.model;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.BooleanUtils;

import java.util.Arrays;
import java.util.List;

@Data
@Builder
public class Market {

    public static Market valid() {
        return Market.builder()
                .eventId("eventId")
                .id("id")
                .name("name")
                .displayed(true)
                .suspended(false)
                .outcomes(Arrays.asList(Outcome.valid()))
                .build();
    }

    public static Market fromInputFeed(List<String> inputFeed) {
        return Market.builder()
                .eventId(inputFeed.get(0))
                .id(inputFeed.get(1))
                .name(inputFeed.get(2))
                .displayed(BooleanUtils.toBoolean(Integer.parseInt(inputFeed.get(3))))
                .suspended(BooleanUtils.toBoolean(Integer.parseInt(inputFeed.get(4))))
                .build();
    }

    public void addOutcome(Outcome outcome) {
        this.outcomes.add(outcome);
    }

    public void update(Market market) {
        eventId = market.getEventId();
        id = market.getId();
        name = market.getName();
        displayed = market.isDisplayed();
        suspended = market.isSuspended();
    }

    String eventId;
    String id;
    String name;
    boolean displayed;
    boolean suspended;

    List<Outcome> outcomes;
}
