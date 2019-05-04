package com.cf.skytest.model;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.BooleanUtils;

import java.util.List;

@Data
@Builder
public class Market {

    public static Market fromInputFeed(List<String> inputFeed) {
        return Market.builder().id(inputFeed.get(0))
                .name(inputFeed.get(1))
                .displayed(BooleanUtils.toBoolean(Integer.parseInt(inputFeed.get(2))))
                .suspended(BooleanUtils.toBoolean(Integer.parseInt(inputFeed.get(3))))
                .build();
    }

    String id;
    String name;
    boolean displayed;
    boolean suspended;
}
