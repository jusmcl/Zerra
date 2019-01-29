package com.zerra.common.world.entity.attrib;

public class BooleanAttribute implements Attribute<Boolean>
{
	private String name;
	private boolean defaultValue;

	protected BooleanAttribute(String name, boolean defaultValue)
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
	public Boolean getDefaultValue()
	{
		return defaultValue;
	}
}