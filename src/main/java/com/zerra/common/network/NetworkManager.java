package com.zerra.common.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zerra.common.Packet;

public class NetworkManager {

    private static final Logger LOGGER = LogManager.getLogger("Network Manager");

    private NetPacketBuilder builder = new NetPacketBuilder();

    Socket socket = new Socket();

    OutputStream out;

    InputStream in;

    public void connect(String ip, int port) {
        try {
            socket.connect(new InetSocketAddress(ip, port));
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (IOException e) {
            System.err.println("Failed to connect");
            // TODO: send state to renderer
            return;
        }

        // TODO: setup encryption

        /*
         * TODO: follow protocol; 1. Send user to server (username, token, development build and so on) 2. Request acces to world (will return true or false; on true connect, on false show player 'server refused connection message') 3. If access provided, request world and playerdata 4. On data retrieve, setup update request loop and send a playerjoinworld packet
         */
    }

    public void sendPacket(Packet packet) throws IOException {
        sendPacket(packet, Callback.EMPTY);
    }

    public void sendPacket(Packet packet, Callback callback) throws IOException {
        out.write(builder.buildPacket(packet).getBytes());
        if (callback != Callback.EMPTY) {
            Scanner scanner = new Scanner(in);
            callback.onResponse(builder.fromString(scanner.next()));
            scanner.close();
        }
    }

    public static interface Callback {

        Callback EMPTY = packet -> {
        };

        void onResponse(Packet packet);

    }

    public static Logger logger() {
        return LOGGER;
    }
}
