package com.cf.skytest.model;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.BooleanUtils;

import java.util.List;

@Data
@Builder
public class Outcome {

    public static Outcome valid() {
        return Outcome.builder()
                .marketId("marketId")
                .id("id")
                .name("name")
                .displayed(true)
                .suspended(false)
                .build();
    }

    public static Outcome fromInputFeed(List<String> inputFeed) {
        return Outcome.builder()
                .marketId(inputFeed.get(0))
                .id(inputFeed.get(1))
                .name(inputFeed.get(2))
                .price(inputFeed.get(3))
                .displayed(BooleanUtils.toBoolean(Integer.parseInt(inputFeed.get(4))))
                .suspended(BooleanUtils.toBoolean(Integer.parseInt(inputFeed.get(5))))
                .build();
    }

    String marketId;
    String id;
    String name;
    String price;
    boolean displayed = false;
    boolean suspended = false;
}
