package com.zerra.common.world.storage.sdf;

public class SDFParseException extends RuntimeException {
    public SDFParseException(String message) {
        super(message);
    }

    public SDFParseException(String message, Object... args) {
        this(String.format(message, args));
    }
}
