package com.zerra.common;

import com.google.gson.JsonObject;

public interface Packet {

    JsonObject toJson();

    Packet fromJson();

}