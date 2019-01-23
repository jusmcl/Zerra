package com.zerra.client.util;

import java.io.InputStream;

import com.zerra.common.Reference;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * A location that leads to an asset in the resources folder. Used to have easy
 * control over ALL locations.
 * 
 * @author Ocelot5836
 */
public class ResourceLocation
{

	private String domain;
	private String location;

	public ResourceLocation(String location)
	{
		String[] resourceLocationRaw = location.split(":", 2);
		if (resourceLocationRaw.length > 1)
		{
			this.domain = resourceLocationRaw[0];
			this.location = resourceLocationRaw[1];
		} else
		{
			this.domain = Reference.DOMAIN;
			this.location = location;
		}
	}

	public ResourceLocation(String domain, String location)
	{
		this.domain = domain;
		this.location = location;
	}

	public ResourceLocation(ResourceLocation location)
	{
		this.domain = location.domain;
		this.location = location.location;
	}

	public ResourceLocation(ResourceLocation folder, String location)
	{
		this.location = folder.domain + ":" + folder.location + "/" + location;
	}

	/**
	 * @return The folder to get the assets from. Ex '/assets/domain'
	 */
	public String getDomain()
	{
		return domain;
	}

	/**
	 * @return The actual location to the asset. Used after domain as
	 *         '/assets/domain/location'
	 */
	public String getLocation()
	{
		return location;
	}

	/**
	 * @return The path to the resource
	 */
	public String getPath()
	{
		return "/assets/" + this.domain + "/" + this.location;
	}

	/**
	 * @return An input stream that leads strait to the asset
	 */
	public InputStream getInputStream()
	{
		return ResourceLocation.class.getResourceAsStream(this.getPath());
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		result = 31 * result + (this.domain == null ? 0 : this.domain.hashCode());
		result = 31 * result + (this.location == null ? 0 : this.location.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		} else if (obj instanceof ResourceLocation)
		{
			ResourceLocation resourcelocation = (ResourceLocation) obj;
			return this.domain.equals(resourcelocation.domain) && this.location.equals(resourcelocation.location);
		}
		return false;
	}

	@Override
	public String toString()
	{
		return this.domain + ":" + this.location;
	}
}