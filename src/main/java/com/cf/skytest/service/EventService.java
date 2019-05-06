package com.cf.skytest.service;

import com.cf.skytest.model.Event;
import com.cf.skytest.model.Market;
import com.cf.skytest.model.Outcome;
import com.cf.skytest.model.Packet;
import com.cf.skytest.repo.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository repository;

    @Value("${packetreader.host}")
    private String packetReaderHost;
    @Value("${packetreader.port}")
    private String packetReaderPort;
    @Value("${packetreader.packetstoread}")
    private String packetsToRead;

    @Autowired
    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public List<Event> getEvents() {
        return repository.findAll();
    }

    public void readPackets() throws IOException {
        repository.deleteAll(); // Just give us a fresh start for each application run
        System.out.println("Reading " + packetsToRead + " packets...");
        int count = 0;
        try (PacketReader packetReader = new PacketReader(packetReaderHost, Integer.parseInt(packetReaderPort))) {
            while (count < Integer.parseInt(packetsToRead)) {
                count++;
                Packet packet = packetReader.readPacket();
                switch (packet.getType()) {
                    case "event":
                        processEvent(packet);
                        break;

                    case "market":
                        processMarket(packet);
                        break;

                    case "outcome":
                        processOutcome(packet);
                        break;

                    default:
                        // Shouldn't get here
                }
            }
        }
        System.out.println("...done");
    }

    private void processEvent(Packet packet) {
        Event event = Event.fromInputFeed(packet.getBodyElements());

        if (packet.isCreate()) {
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

    private void processMarket(Packet packet) {
        Market market = Market.fromInputFeed(packet.getBodyElements());
        Optional<Event> opt = repository.findById(market.getEventId());
        opt.ifPresent(existingEvent -> {
            if (packet.isCreate()) {
                existingEvent.addMarket(market);
            }
            else {
                Market existingMarket = existingEvent.getMarketById(market.getId());
                existingMarket.update(market);
            }
            repository.save(existingEvent);
        });
    }

    private void processOutcome(Packet packet) {
        Outcome outcome = Outcome.fromInputFeed(packet.getBodyElements());
        if (packet.isCreate()) {
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
