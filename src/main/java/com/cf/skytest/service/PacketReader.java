package com.cf.skytest.service;

import com.cf.skytest.model.StreamPacket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class PacketReader implements AutoCloseable {

    private Socket socket;
    private BufferedReader reader;

    public PacketReader(Socket socket, BufferedReader reader) {
        this.socket = socket;
        this.reader = reader;
    }

    public PacketReader(String host, int port) throws IOException {
        socket = new Socket(host, port);
        InputStream input = socket.getInputStream();
        reader = new BufferedReader(new InputStreamReader(input));
    }

    public void close() throws IOException {
        reader.close();
        socket.close();
    }

    public StreamPacket readPacket() throws IOException {
        String line = reader.readLine();
        String regex = "(?<!\\\\)" + Pattern.quote("|");
        List<String> list = Arrays.asList(line.split(regex));
        list = list.stream().map(s -> s.replace("\\|", "|")).collect(Collectors.toList());
        return StreamPacket.fromInputFeed(list);
    }
}
