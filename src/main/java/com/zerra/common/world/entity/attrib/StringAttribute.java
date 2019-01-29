package com.zerra.common.world.entity.attrib;

public class StringAttribute implements Attribute<String>
{
	private String name;
	private String defaultValue;

	protected StringAttribute(String name, String defaultValue)
	{
		this.name = name;
		this.defaultValue = defaultValue;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String getDefaultValue()
	{
		return defaultValue;
	}
}