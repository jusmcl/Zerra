package com.zerra.common.util;

import com.zerra.common.registry.RegistryNameable;

public class Factory<T> implements RegistryNameable
{

	protected String registryName;
	protected final Class<T> type;

	public Factory(String registryName, Class<T> type)
	{
		this.registryName = registryName;
		this.type = type;
	}

	@Override
	public void setDomain(String domain)
	{
		this.registryName = RegistryNameable.injectDomain(this.registryName, domain);
	}

	@Override
	public String getRegistryName()
	{
		return this.registryName;
	}

	public Class<T> getType()
	{
		return this.type;
	}

	/**
	 * Gets a new instance of this factory's {@link Factory#type}
	 */
	public T getNewInstance()
	{
		return MiscUtils.createNewInstance(this.type, new Class[]{String.class}, registryName);
	}
}
