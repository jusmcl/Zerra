package com.zerra.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class NetPacketBuilder {

    private static final Gson gson = new GsonBuilder().create();

    public String buildPacket(Packet packet){
        JsonObject object = packet.toJson();
        StringBuilder packetBuilder = new StringBuilder();
        packetBuilder.append(object.getAsString());
        packetBuilder.append(":|:");
        packetBuilder.append(packet.getClass().getName());
        packetBuilder.append(';');
        return packetBuilder.toString();
    }


    public Packet fromString(String string){
        String[] parts = string.split(":|:", 2);
        String json = parts[0];
        try {
            Class<? extends Packet> c = (Class<? extends Packet>) ClassLoader.getSystemClassLoader().loadClass(parts[1]);
            return gson.fromJson(json, c);
        } catch (ClassNotFoundException | ClassCastException e) {
            e.printStackTrace();
            //TODO: Report FATAL to Zerra
            return null;
        }
    }
}
