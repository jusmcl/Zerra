package com.zerra.common.world.data;

import javax.annotation.Nonnull;

import com.zerra.common.util.UBObjectWrapper;
import com.zerra.common.world.storage.Storable;

public abstract class WorldData implements Storable
{

	private String registryName;

	/**
	 * The registryName argument will be supplied from the {@link WorldDataFactory}
	 */
	public WorldData(String registryName)
	{
		this.registryName = registryName;
	}

	public String getRegistryName()
	{
		return registryName;
	}

	@Nonnull
	@Override
	public UBObjectWrapper writeToUBO(@Nonnull UBObjectWrapper ubo)
	{
		ubo.setString("name", registryName);
		return ubo;
	}

	@Override
	public void readFromUBO(@Nonnull UBObjectWrapper ubo)
	{
		registryName = ubo.getString("name");
	}
}
