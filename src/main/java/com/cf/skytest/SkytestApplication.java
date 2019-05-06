package com.cf.skytest;

import com.cf.skytest.model.Event;
import com.cf.skytest.model.Market;
import com.cf.skytest.model.StreamPacket;
import com.cf.skytest.repo.EventRepository;
import com.cf.skytest.service.PacketReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Optional;

@SpringBootApplication
public class SkytestApplication implements CommandLineRunner {

	@Autowired
	private EventRepository eventRepository;

	public static void main(String[] args) {
		SpringApplication.run(SkytestApplication.class, args);
	}

	@Override
	public void run(String... args) throws IOException {

		eventRepository.deleteAll();

		System.out.println("Reading packets...");

		try (PacketReader packetReader = new PacketReader("192.168.99.100", 8282)) {

			int count = 0;
			while (count < 100) {
				StreamPacket streamPacket = packetReader.readPacket();
				System.out.println(streamPacket);

				switch (streamPacket.getType()) {
					case "event":
						Event event = Event.fromInputFeed(streamPacket.getBodyElements());

						if (streamPacket.isCreate()) {
							eventRepository.save(event);
						}
						else {
							Optional<Event> opt = eventRepository.findById(event.getId());
							if (opt.isPresent()) {
								Event existingEvent = opt.get();
								existingEvent.update(event);
								eventRepository.save(existingEvent);
							}
						}
						break;

					case "market":
						Market market = Market.fromInputFeed(streamPacket.getBodyElements());
						Optional<Event> opt = eventRepository.findById(market.getEventId());
						if (opt.isPresent()) {
							Event existingEvent = opt.get();
							if (streamPacket.isCreate()) {
								existingEvent.addMarket(market);
							}
							else {
								existingEvent.updateMarket(market);
							}
							eventRepository.save(existingEvent);
						}

					default:
						// Nothing yet
				}
				count++;
			}
		}

		System.out.println("...done");

		eventRepository.findAll().forEach(System.out::println);
	}

}
