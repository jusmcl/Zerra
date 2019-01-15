package com.zerra.common.world.storage;

import com.zerra.common.util.UBObjectWrapper;

import javax.annotation.Nonnull;

public interface Storable {

    /**
     * Serialises any data that needs persisting into a {@link UBObjectWrapper} object
     *
     * @return {@link UBObjectWrapper} object containing serialised data
     */
	@Nonnull
    UBObjectWrapper writeToUBO();

    /**
     * Deserialises the data within a {@link UBObjectWrapper} object
     *
     * @param ubo {@link UBObjectWrapper} object with serialised data
     */
	void readFromUBO(@Nonnull UBObjectWrapper ubo);
}
