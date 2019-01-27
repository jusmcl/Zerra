package com.zerra.common.world.entity.attrib;

public class NumberAttribute implements Attribute<Number>
{
	private Number defaultValue;
	private Number value;

	protected NumberAttribute(Number defaultValue)
	{
		this.defaultValue = defaultValue;
		this.value = defaultValue;
	}

	@Override
	public Number getDefaultValue()
	{
		return defaultValue;
	}

	@Override
	public Number getValue()
	{
		return value;
	}

	@Override
	public void setValue(Number value)
	{
		this.value = value;
	}
}