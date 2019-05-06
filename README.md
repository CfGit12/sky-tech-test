# SkyBet FeedMe Technical Test

Source: https://github.com/skybet/feedme-tech-test/

The task has been completed as a Java Spring Boot application.

## How to run

The service relies on the provider feed and a mongodb instance running.
 This has been achieved locally via 
 
1. Create the following docker compose file:

        version: '2'
        services:
          mongo:
            image: mongo
            ports:
              - "27017:27017"
          provider:
              image: sbgfeedme/provider:latest
              ports:
                - "8181:8181"
                - "8282:8282"

2. Run `docker-compose up`

3. Either run the application by importing into IntelliJ and ruunning SkytestApplication.java.
**or** build via maven and run from the command line

`application.properties` features properties for the provider feed host/port and mongo host/port. This should be updated as appropriate before running or building.
Alternatively these values can be overriden at runtime, e.g. `java -jar sky-tech-test-0.0.1-SNAPSHOT.jar --packetreader.packetstoread=100`

The application will read a certain number of packets (1000) by default. This is to prevent overloading disk space in this simple implementation.

A swagger front end (http://localhost:8080/swagger-ui.html) can be used to query what data has been saved to the database.

## Testing

The easiest way to run the tests is via the Test Maven goal in IntelliJ. Alternatively this can be done on the command line with Maven.

Tests have been written to provide confidence in terms of parsing the input feed and converting to Java objects. The coverage isn't full due to time.

Likewise there would ideally be testing that writing and retrieving from the database is correct. This has been done via inspection for now.

## Language, framework, and remarks

Java and Spring Boot have been chosen. Spring boot provides a quick way of getting an application up and running with sensible configuration/pom/etc.

The web and data frameworks simplify that side of things allowing a focus on connecting to and processing the provider feed.

I have not explicitly converted my Java objects to JSON and instead have relied on the frameworks for this. 

I am trusting that the feed will only send me valid data. In reality this would require validation prior to processing. Similarily if e.g. a request comes in to update an outcome which has not been received (due to restarting the application for instance) the application will quietly just do nothing. In reality we would want to handle this.

There's plenty of refactoring and tidy up I'd like to have got done but hope I've got the balance of showing skills vs knowing when to stop in such a task right.