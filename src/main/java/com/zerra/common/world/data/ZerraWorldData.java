package com.zerra.common.world.data;

import com.zerra.common.util.UBObjectWrapper;

import javax.annotation.Nonnull;

public class ZerraWorldData extends WorldData {

	//TODO: Store extra world data in ZerraWorldData

	public ZerraWorldData() {
		super("zerra");
	}

	@Nonnull
	@Override
	public UBObjectWrapper writeToUBO(@Nonnull UBObjectWrapper ubo) {
		return ubo;
	}

	@Override
	public void readFromUBO(@Nonnull UBObjectWrapper ubo) {

	}
}
