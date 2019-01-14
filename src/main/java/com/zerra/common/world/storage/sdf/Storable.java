package com.zerra.common.world.storage.sdf;

public interface Storable {

    /**
     * Serialises any data that needs persisting into a SimpleDataFormat object
     *
     * @return SimpleDataFormat object containing serialised data
     */
    SimpleDataFormat writeToSDF();

    /**
     * Deserialises the data within a SimpleDataFormat object
     *
     * @param sdf SimpleDataFormat object with serialised data
     */
    void readFromSDF(SimpleDataFormat sdf);
}
