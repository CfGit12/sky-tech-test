package com.cf.skytest.service;

import com.cf.skytest.model.StreamPacket;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PacketReaderTest {

    private PacketReader packetReader;
    private Socket socket;
    private BufferedReader reader;

    private static final String VALID_EVENT_PACKET = "|2054|create|event|1497359166352|ee4d2439-e1c5-4cb7-98ad-9879b2fd84c2|Football|Sky Bet League Two|\\|Accrington\\| vs \\|Cambridge\\||1497359216693|0|1|";
    private static final String VALID_OUTCOME_PACKET = "|2054|update|outcome|1497359166352|marketId|outcomeId|name||0|1|";

    @Before
    public void setUp() {
        socket = mock(Socket.class);
        reader = mock(BufferedReader.class);
        packetReader = new PacketReader(socket, reader);
    }

    @Test
    public void givenValidInput_whenReadEventPacket_thenReturnPacket() throws IOException {
        when(reader.readLine()).thenReturn(VALID_EVENT_PACKET);

        StreamPacket packet = packetReader.readPacket();

        assertEquals(packet.getMsgId(),"2054");
        assertEquals(packet.getOperation(), "create");
        assertEquals(packet.getType(), "event");
        assertEquals(packet.getTimestamp(), LocalDateTime.ofInstant(Instant.ofEpochMilli(1497359166352l), ZoneId.systemDefault()));
        assertThat(packet.getBodyElements(), CoreMatchers.hasItems("ee4d2439-e1c5-4cb7-98ad-9879b2fd84c2", "Football", "Sky Bet League Two", "|Accrington| vs |Cambridge|", "1497359216693", "0", "1"));
    }

    @Test
    public void givenValidInput_whenReadOutcomePacket_thenReturnPacket() throws IOException {
        when(reader.readLine()).thenReturn(VALID_OUTCOME_PACKET);

        StreamPacket packet = packetReader.readPacket();

        assertEquals(packet.getMsgId(),"2054");
        assertEquals(packet.getOperation(), "update");
        assertEquals(packet.getType(), "outcome");
        assertEquals(packet.getTimestamp(), LocalDateTime.ofInstant(Instant.ofEpochMilli(1497359166352l), ZoneId.systemDefault()));
        assertThat(packet.getBodyElements(), CoreMatchers.hasItems("marketId", "outcomeId", "name", "", "0", "1"));
    }

}