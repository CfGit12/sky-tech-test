package com.cf.skytest.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class MarketTest {

    @Test
    public void givenValidInput_whenFromInputFeed_thenReturnMarket() {
        String eventId = "eventId";
        String id = "id";
        String name = "name";
        String displayed = "0";
        String suspended = "1";
        List<String> feed = Arrays.asList(eventId, id, name, displayed, suspended);
        Market market = Market.fromInputFeed(feed);
        Assert.assertEquals(market.getEventId(), eventId);
        Assert.assertEquals(market.getId(), id);
        Assert.assertEquals(market.getName(), name);
        Assert.assertFalse(market.isDisplayed());
        Assert.assertTrue(market.isSuspended());
    }

}