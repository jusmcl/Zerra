package com.zerra.common.world.data;

import javax.annotation.Nonnull;

import com.zerra.client.ZerraClient;
import com.zerra.common.util.UBObjectWrapper;

public class ZerraWorldData extends WorldData
{

	// TODO: Store extra world data in ZerraWorldData

	public ZerraWorldData(String name)
	{
		super(name);
	}

	@Nonnull
	@Override
	public UBObjectWrapper writeToUBO(@Nonnull UBObjectWrapper ubo)
	{
		ubo.setLong("seed", ZerraClient.getInstance().getWorld().getSeed());
		return ubo;
	}

	@Override
	public void readFromUBO(@Nonnull UBObjectWrapper ubo)
	{
		System.out.println(ubo.getLong("seed"));
	}
}
