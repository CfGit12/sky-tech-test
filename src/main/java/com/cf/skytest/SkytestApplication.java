package com.cf.skytest;

import com.cf.skytest.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class SkytestApplication implements CommandLineRunner {

	@Autowired
	private EventService eventService;

	public static void main(String[] args) {
		SpringApplication.run(SkytestApplication.class, args);
	}

	@Override
	public void run(String... args) throws IOException {

		eventService.readPackets();

		//System.out.println("...done");

		//eventRepository.findAll().forEach(System.out::println);
	}

}
