package com.cf.skytest.repo;

import com.cf.skytest.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EventRepository extends MongoRepository<Event, String> {

    Optional<Event> findByMarketsId(String id);
    Optional<Event> findByMarketsOutcomesId(String id);
}
