package com.cf.skytest.model;

import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

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
        assertEquals(event.getId(), id);
        assertEquals(event.getCategory(), category);
        assertEquals(event.getSubCategory(), subCategory);
        assertEquals(event.getStartTime(), LocalDateTime.ofInstant(now, ZoneId.systemDefault()));
        assertEquals(event.getName(), name);
        Assert.assertFalse(event.isDisplayed());
        Assert.assertTrue(event.isSuspended());
    }

    @Test
    public void givenValidEvent_whenUpdateEvent_thenUpdateEvent() {
        Event initialEvent = Event.valid();

        LocalDateTime now = LocalDateTime.now();
        Event newEvent = Event.builder()
                .id("newId")
                .category("newCategory")
                .subCategory("newSubCategory")
                .name("newName")
                .startTime(now)
                .displayed(false)
                .suspended(true)
                .build();

        initialEvent.update(newEvent);

        assertEquals(initialEvent.getId(), "newId");
        assertEquals(initialEvent.getCategory(), "newCategory");
        assertEquals(initialEvent.getSubCategory(), "newSubCategory");
        assertEquals(initialEvent.getName(), "newName");
        assertEquals(initialEvent.getStartTime(), now);
        assertFalse(initialEvent.isDisplayed());
        assertTrue((initialEvent.isSuspended()));
    }

}