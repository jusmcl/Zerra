package com.zerra.common.util;

import java.util.UUID;

import com.devsmart.ubjson.UBArray;
import com.devsmart.ubjson.UBObject;
import com.devsmart.ubjson.UBValue;
import com.devsmart.ubjson.UBValueFactory;

/**
 * Wraps the {@link UBObject} to provide simpler methods mostly for getting and setting
 */
public class UBObjectWrapper {

    /**
     * The wrapped {@link UBObject}
     */
    private UBObject ubObject;

    /**
     * Creates a new empty {@link UBObject}
     */
    public UBObjectWrapper() {
        ubObject = UBValueFactory.createObject();
    }

    /**
     * Wraps the given {@link UBObject}
     */
    public UBObjectWrapper(UBObject ubObject) {
        this.ubObject = ubObject;
    }

    /**
     * Helper method to create a {@link UBObjectWrapper} from a {@link UBValue} if possible
     * Returns null if the value is not a {@link UBObject}
     *
     * @param ubValue Value to try use as a UBObject
     * @return {@link UBObjectWrapper} or null
     */
    public static UBObjectWrapper create(UBValue ubValue) {
        return ubValue.isObject() ? new UBObjectWrapper(ubValue.asObject()) : null;
    }

    /**
     * Gets the {@link UBObject} that this class is wrapping
     */
    public UBObject getWrappedUBObject() {
        return ubObject;
    }

    /**
     * Sets a generic {@link UBValue} to {@link UBObjectWrapper#ubObject}
     * Used internally in this class only
     */
    private void set(String key, UBValue value) {
        ubObject.put(key, value);
    }

    /**
     * Sets a boolean to the wrapped {@link UBObject}
     */
    public void setBoolean(String key, boolean value) {
        set(key, UBValueFactory.createBool(value));
    }

    /**
     * Sets a byte to the wrapped {@link UBObject}
     */
    public void setByte(String key, byte value) {
        setLong(key, value);
    }

    /**
     * Sets a short to the wrapped {@link UBObject}
     */
    public void setShort(String key, short value) {
        setLong(key, value);
    }

    /**
     * Sets a integer to the wrapped {@link UBObject}
     */
    public void setInt(String key, int value) {
        setLong(key, value);
    }

    /**
     * Sets a long to the wrapped {@link UBObject}
     */
    public void setLong(String key, long value) {
        set(key, UBValueFactory.createInt(value));
    }

    /**
     * Sets a float to the wrapped {@link UBObject}
     */
    public void setFloat(String key, float value) {
        set(key, UBValueFactory.createFloat32(value));
    }

    /**
     * Sets a double to the wrapped {@link UBObject}
     */
    public void setDouble(String key, double value) {
        set(key, UBValueFactory.createFloat64(value));
    }

    /**
     * Sets a {@link String} to the wrapped {@link UBObject}
     */
    public void setString(String key, String value) {
        set(key, UBValueFactory.createString(value));
    }

    /**
     * Sets a {@link UUID} to the wrapped {@link UBObject}
     */
    public void setUUID(String key, UUID uuid) {
        setLong(key + "_m", uuid.getMostSignificantBits());
        setLong(key + "_l", uuid.getLeastSignificantBits());
    }

    /**
     * Sets a null to the wrapped {@link UBObject}
     */
    public void setNull(String key) {
        set(key, UBValueFactory.createNull());
    }

    /**
     * Sets a {@link UBObject} to the wrapped {@link UBObject}
     */
    public void setUBObject(String key, UBObject value) {
        set(key, value);
    }

    /**
     * Sets a {@link UBObjectWrapper} to the wrapped {@link UBObject}
     */
    public void setUBObject(String key, UBObjectWrapper value) {
        setUBObject(key, value.getWrappedUBObject());
    }

    /**
     * Tries to create a {@link UBValue} for this object
     * Will return true if creation was successful and the object was added
     * This should work for most primitive types as well as {@link java.util.Map}s, {@link Iterable}s and
     * {@link UBArray}s
     *
     * @param key   Key
     * @param value {@link Object} value
     * @return True if successful
     */
    public boolean setObject(String key, Object value) {
        UBValue ubValue = UBValueFactory.createValue(value);
        boolean isNotNull = ubValue != null;
        if (isNotNull) {
            set(key, ubValue);
        }
        return isNotNull;
    }

    /**
     * Gets a generic {@link UBValue} from {@link UBObjectWrapper#ubObject}
     * Used internally in this class only
     */
    private UBValue get(String key) {
        return ubObject.get(key);
    }

    /**
     * Returns true if the key-value pair does not exist, or the key is mapped to null
     */
    public boolean isNull(String key) {
        UBValue value = get(key);
        return value == null || value.isNull();
    }

    /**
     * Gets a {@link Boolean} from the wrapped {@link UBObject}
     * Returns null if the value was not a boolean or the key-value pair did not exist
     */
    public Boolean getBoolean(String key) {
        UBValue value = get(key);
        return value.isBool() ? value.asBool() : null;
    }

    /**
     * Gets a boolean from the wrapped {@link UBObject}
     * If the value was null, then will return false
     */
    public boolean getBooleanSafe(String key) {
        Boolean value = getBoolean(key);
        return value != null ? value : false;
    }

    /**
     * Gets a {@link Byte} from the wrapped {@link UBObject}
     * Returns null if the value was not a number or the key-value pair did not exist
     */
    public Byte getByte(String key) {
        UBValue value = get(key);
        return value != null && value.isNumber() ? value.asByte() : null;
    }

    /**
     * Gets a byte from the wrapped {@link UBObject}
     * If the value was null, then will return 0
     */
    public byte getByteSafe(String key) {
        Byte value = getByte(key);
        return value != null ? value : 0;
    }

    /**
     * Gets a {@link Short} from the wrapped {@link UBObject}
     * Returns null if the value was not a number or the key-value pair did not exist
     */
    public Short getShort(String key) {
        UBValue value = get(key);
        return value != null && value.isNumber() ? value.asShort() : null;
    }

    /**
     * Gets a short from the wrapped {@link UBObject}
     * If the value was null, then will return 0
     */
    public short getShortSafe(String key) {
        Short value = getShort(key);
        return value != null ? value : 0;
    }

    /**
     * Gets a {@link Integer} from the wrapped {@link UBObject}
     * Returns null if the value was not a number or the key-value pair did not exist
     */
    public Integer getInt(String key) {
        UBValue value = get(key);
        return value != null && value.isNumber() ? value.asInt() : null;
    }

    /**
     * Gets an integer from the wrapped {@link UBObject}
     * If the value was null, then will return 0
     */
    public int getIntSafe(String key) {
        Integer value = getInt(key);
        return value != null ? value : 0;
    }

    /**
     * Gets a {@link Long} from the wrapped {@link UBObject}
     * Returns null if the value was not a number or the key-value pair did not exist
     */
    public Long getLong(String key) {
        UBValue value = get(key);
        return value != null && value.isNumber() ? value.asLong() : null;
    }

    /**
     * Gets a long from the wrapped {@link UBObject}
     * If the value was null, then will return 0
     */
    public long getLongSafe(String key) {
        Long value = getLong(key);
        return value != null ? value : 0;
    }

    /**
     * Gets a {@link Float} from the wrapped {@link UBObject}
     * Returns null if the value was not a number or the key-value pair did not exist
     */
    public Float getFloat(String key) {
        UBValue value = get(key);
        return value != null && value.isNumber() ? value.asFloat32() : null;
    }

    /**
     * Gets a float from the wrapped {@link UBObject}
     * If the value was null, then will return 0
     */
    public float getFloatSafe(String key) {
        Float value = getFloat(key);
        return value != null ? value : 0;
    }

    /**
     * Gets a {@link Double} from the wrapped {@link UBObject}
     * Returns null if the value was not a number or the key-value pair did not exist
     */
    public Double getDouble(String key) {
        UBValue value = get(key);
        return value != null && value.isNumber() ? value.asFloat64() : null;
    }

    /**
     * Gets a double from the wrapped {@link UBObject}
     * If the value was null, then will return 0
     */
    public double getDoubleSafe(String key) {
        Double value = getDouble(key);
        return value != null ? value : 0;
    }

    /**
     * Gets a {@link String} from the wrapped {@link UBObject}
     * Returns null if the value was not a number or the key-value pair did not exist
     */
    public String getString(String key) {
        UBValue value = get(key);
        if (value == null) {
            return null;
        } else if (value.isString()) {
            return value.asString();
        } else if (value.isNumber()) {
            return String.valueOf(value.asLong());
        } else {
            return null;
        }
    }

    /**
     * Gets a {@link String} from the wrapped {@link UBObject}
     * If the value was null, then will return an empty String
     */
    public String getStringSafe(String key) {
        String value = getString(key);
        return value != null ? value : "";
    }

    /**
     * Gets a {@link UUID} from the wrapped {@link UBObject}
     * Returns null if either of the component parts of the UUID do not exist
     */
    public UUID getUUID(String key) {
        Long most = getLong(key + "_m");
        Long least = getLong(key + "_l");
        return most != null && least != null ? new UUID(most, least) : null;
    }

    /**
     * Gets a {@link UBObject} from the wrapped {@link UBObject}
     * Returns null if the value was not an object or the key-value pair did not exist
     */
    public UBObject getUBObject(String key) {
        UBValue value = get(key);
        return value != null && value.isObject() ? value.asObject() : null;
    }

    /**
     * Gets a {@link UBObjectWrapper} from the wrapped {@link UBObject}
     * Returns null if the value was not an object or the key-value pair did not exist
     */
    public UBObjectWrapper getUBObjectWrapped(String key) {
        UBObject value = getUBObject(key);
        return value != null ? new UBObjectWrapper(value) : null;
    }

    /**
     * Gets a {@link UBArray} from the wrapped {@link UBObject}
     * Returns null if the value was not an array or the key-value pair did not exist
     */
    public UBArray getUBArray(String key) {
        UBValue value = get(key);
        return value != null && value.isArray() ? value.asArray() : null;
    }
}
