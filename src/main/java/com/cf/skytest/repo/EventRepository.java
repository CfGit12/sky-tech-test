package com.cf.skytest.repo;

import com.cf.skytest.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<Event, String> {

    Event findByName(String name);
}
