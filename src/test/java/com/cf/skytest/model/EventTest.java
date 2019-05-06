package com.cf.skytest.model;

import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

public class EventTest {

    @Test
    public void givenValidInput_whenFromInputFeed_thenCreateEvent() {
        String id = "id";
        String category = "category";
        String subCategory = "subCategory";
        String name = "name";
        Instant now = Instant.now();
        String startTime = Long.toString(now.toEpochMilli());
        String displayed = "0";
        String suspended = "1";
        List<String> feed = Arrays.asList(id, category, subCategory, name, startTime, displayed, suspended);
        Event event = Event.fromInputFeed(feed);
        Assert.assertEquals(event.getId(), id);
        Assert.assertEquals(event.getCategory(), category);
        Assert.assertEquals(event.getSubCategory(), subCategory);
        Assert.assertEquals(event.getStartTime(), LocalDateTime.ofInstant(now, ZoneId.systemDefault()));
        Assert.assertEquals(event.getName(), name);
        Assert.assertFalse(event.isDisplayed());
        Assert.assertTrue(event.isSuspended());
    }

}