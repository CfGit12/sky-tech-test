package com.cf.skytest.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class OutcomeTest {

    @Test
    public void givenValidInput_whenFromInputFeed_thenReturnOutcome() {
        String marketId = "marketId";
        String id = "id";
        String name = "name";
        String price = "price";
        String displayed = "0";
        String suspended = "1";
        List<String> feed = Arrays.asList(marketId, id, name, price, displayed, suspended);
        Outcome outcome = Outcome.fromInputFeed(feed);
        Assert.assertEquals(outcome.getMarketId(), marketId);
        Assert.assertEquals(outcome.getId(), id);
        Assert.assertEquals(outcome.getName(), name);
        Assert.assertEquals(outcome.getPrice(), price);
        Assert.assertFalse(outcome.isDisplayed());
        Assert.assertTrue(outcome.isSuspended());
    }
}