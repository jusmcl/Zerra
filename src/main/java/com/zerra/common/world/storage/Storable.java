package com.zerra.common.world.storage;

import javax.annotation.Nonnull;

import com.zerra.common.util.UBObjectWrapper;

public interface Storable {

    /**
     * Serialises any data that needs persisting into a {@link UBObjectWrapper} object
     *
     * @return {@link UBObjectWrapper} object containing serialised data
     * @param ubo
     */
	@Nonnull
    UBObjectWrapper writeToUBO(@Nonnull UBObjectWrapper ubo);

    /**
     * Deserialises the data within a {@link UBObjectWrapper} object
     *
     * @param ubo {@link UBObjectWrapper} object with serialised data
     */
	void readFromUBO(@Nonnull UBObjectWrapper ubo);
}
