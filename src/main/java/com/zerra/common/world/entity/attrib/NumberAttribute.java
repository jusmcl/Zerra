package com.zerra.common.world.entity.attrib;

public class NumberAttribute<T extends Number> implements Attribute<T>
{
	private String name;
	private T defaultValue;

	protected NumberAttribute(String name, T defaultValue)
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
	public T getDefaultValue()
	{
		return defaultValue;
	}
}