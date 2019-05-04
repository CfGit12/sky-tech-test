package com.cf.skytest.model;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.BooleanUtils;

import java.util.List;

@Data
@Builder
public class Outcome {

    public static Outcome fromInputFeed(List<String> inputFeed) {
        return Outcome.builder().id(inputFeed.get(0))
                .name(inputFeed.get(1))
                .price(inputFeed.get(2))
                .displayed(BooleanUtils.toBoolean(Integer.parseInt(inputFeed.get(3))))
                .suspended(BooleanUtils.toBoolean(Integer.parseInt(inputFeed.get(4))))
                .build();
    }

    String id;
    String name;
    String price;
    boolean displayed = false;
    boolean suspended = false;
}
