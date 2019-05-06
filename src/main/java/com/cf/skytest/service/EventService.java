package com.cf.skytest.service;

import com.cf.skytest.model.Event;
import com.cf.skytest.model.Market;
import com.cf.skytest.model.Outcome;
import com.cf.skytest.model.StreamPacket;
import com.cf.skytest.repo.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository repository;

    @Autowired
    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public List<Event> getEvents() {
        return repository.findAll();
    }

    public void readPackets() throws IOException {
        repository.deleteAll(); // Just give us a fresh start for each application run
        System.out.println("Reading packets...");
        try (PacketReader packetReader = new PacketReader("192.168.99.100", 8282)) {
            while (true) {
                StreamPacket streamPacket = packetReader.readPacket();
                switch (streamPacket.getType()) {
                    case "event":
                        processEvent(streamPacket);
                        break;

                    case "market":
                        processMarket(streamPacket);
                        break;

                    case "outcome":
                        processOutcome(streamPacket);
                        break;

                    default:
                        // Shouldn't get here
                }
            }
        }
    }

    private void processEvent(StreamPacket streamPacket) {
        Event event = Event.fromInputFeed(streamPacket.getBodyElements());

        if (streamPacket.isCreate()) {
            repository.save(event);
        }
        else {
            Optional<Event> opt = repository.findById(event.getId());
            opt.ifPresent(existingEvent -> {
                existingEvent.update(event);
                repository.save(existingEvent);
            });
        }
    }

    private void processMarket(StreamPacket streamPacket) {
        Market market = Market.fromInputFeed(streamPacket.getBodyElements());
        Optional<Event> opt = repository.findById(market.getEventId());
        opt.ifPresent(existingEvent -> {
            if (streamPacket.isCreate()) {
                existingEvent.addMarket(market);
            }
            else {
                Market existingMarket = existingEvent.getMarketById(market.getId());
                existingMarket.update(market);
            }
            repository.save(existingEvent);
        });
    }

    private void processOutcome(StreamPacket streamPacket) {
        Outcome outcome = Outcome.fromInputFeed(streamPacket.getBodyElements());
        if (streamPacket.isCreate()) {
            Optional<Event> opt = repository.findByMarketsId(outcome.getMarketId());
            opt.ifPresent(existingEvent -> {
                Market existingMarket = existingEvent.getMarketById(outcome.getMarketId());
                existingMarket.addOutcome(outcome);
                repository.save(existingEvent);
            });
        }
        else {
            Optional<Event> opt = repository.findByMarketsOutcomesId(outcome.getId());
            opt.ifPresent(existingEvent -> {
                Market existingMarket = existingEvent.getMarketById(outcome.getMarketId());
                Outcome existingOutcome = existingMarket.getOutcomeById(outcome.getId());
                existingOutcome.update(outcome);
                repository.save(existingEvent);
            });
        }
    }
}
