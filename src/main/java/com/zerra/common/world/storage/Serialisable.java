package com.zerra.common.world.storage;

public interface Serialisable {

    /**
     * Serialises any data that needs persisting into a SimpleDataFormat object
     *
     * @return SimpleDataFormat object containing serialised data
     */
    SimpleDataFormat serialise();

    /**
     * Deserialises the data within a SimpleDataFormat object
     *
     * @param sdf SimpleDataFormat object with serialised data
     */
    void deserialise(SimpleDataFormat sdf);
}
