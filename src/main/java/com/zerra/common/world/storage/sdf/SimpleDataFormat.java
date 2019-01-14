package com.zerra.common.world.storage.sdf;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * A rather generic and simple data structure used for holding any kind of data
 * Mainly used for serialising and deserialising data
 */
public class SimpleDataFormat {
    private static final Pattern KEY_PATTERN = Pattern.compile("^\\w+$");
    private Map<String, Object> data = new HashMap<>();

    public SimpleDataFormat() {

    }

    // Package private method only to be used by SDFFileParser and internal methods in this class
    void set(String key, Object value) {
        if (!KEY_PATTERN.matcher(key).matches()) {
            throw new RuntimeException("The key '%s' is invalid! It must only contain letters, numbers and underscores");
        }
        data.put(key, value);
    }

    public void setBoolean(String key, boolean value) {
        set(key, value);
    }

    public void setByte(String key, byte value) {
        set(key, value);
    }

    public void setShort(String key, short value) {
        set(key, value);
    }

    public void setInt(String key, int value) {
        set(key, value);
    }

    public void setLong(String key, long value) {
        set(key, value);
    }

    public void setFloat(String key, float value) {
        set(key, value);
    }

    public void setDouble(String key, double value) {
        set(key, value);
    }

    public void setString(String key, String value) {
        set(key, value);
    }

    public void setUUID(String key, UUID value) {
        set(key + "_most", value.getMostSignificantBits());
        set(key + "_least", value.getLeastSignificantBits());
    }

    public void setData(String key, SimpleDataFormat sdf) {
        set(key, sdf);
    }

    private <T> T get(String key, Class<T> type) {
        Object value = data.get(key);
        if (!type.isInstance(value)) {
            return null;
        }
        return type.cast(value);
    }

    public Boolean getBoolean(String key) {
        return get(key, Boolean.class);
    }

    public Byte getByte(String key) {
        return get(key, Byte.class);
    }

    public Short getShort(String key) {
        return get(key, Short.class);
    }

    public Integer getInt(String key) {
        return get(key, Integer.class);
    }

    public Long getLong(String key) {
        return get(key, Long.class);
    }

    public Float getFloat(String key) {
        return get(key, Float.class);
    }

    public Double getDouble(String key) {
        return get(key, Double.class);
    }

    public String getString(String key) {
        return get(key, String.class);
    }

    public UUID getUUID(String key) {
        Long most = get(key + "_most", Long.class);
        Long least = get(key + "_least", Long.class);
        return most == null || least == null ? null : new UUID(most, least);
    }

    public SimpleDataFormat getData(String key) {
        return get(key, SimpleDataFormat.class);
    }

    public boolean getBooleanSafe(String key) {
        Boolean value = get(key, Boolean.class);
        return value == null ? false : value;
    }

    public byte getByteSafe(String key) {
        Byte value = get(key, Byte.class);
        return value == null ? (byte) 0 : value;
    }

    public short getShortSafe(String key) {
        Short value = get(key, Short.class);
        return value == null ? (short) 0 : value;
    }

    public int getIntSafe(String key) {
        Integer value = get(key, Integer.class);
        return value == null ? 0 : value;
    }

    public long getLongSafe(String key) {
        Long value = get(key, Long.class);
        return value == null ? 0L : value;
    }

    public float getFloatSafe(String key) {
        Float value = get(key, Float.class);
        return value == null ? 0F : value;
    }

    public double getDoubleSafe(String key) {
        Double value = get(key, Double.class);
        return value == null ? 0D : value;
    }

    public String getStringSafe(String key) {
        String value = get(key, String.class);
        return value == null ? "" : value;
    }
}
