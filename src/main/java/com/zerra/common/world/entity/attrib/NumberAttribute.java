package com.zerra.common.world.entity.attrib;

public class NumberAttribute<T extends Number> implements Attribute<T>
{
	private T defaultValue;
	private T value;

	protected NumberAttribute(T defaultValue)
	{
		this.defaultValue = defaultValue;
		this.value = defaultValue;
	}

	@Override
	public T getDefaultValue()
	{
		return defaultValue;
	}

	@Override
	public T getValue()
	{
		return value;
	}

	@Override
	public void setValue(T value)
	{
		this.value = value;
	}
}