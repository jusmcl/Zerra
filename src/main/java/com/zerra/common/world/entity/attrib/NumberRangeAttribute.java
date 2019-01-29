package com.zerra.common.world.entity.attrib;

public class NumberRangeAttribute<T extends Number> extends NumberAttribute<T> implements RangeAttribute<T>
{
	private T minimumValue;
	private T maximumValue;

	protected NumberRangeAttribute(String name, T defaultValue, T minimumValue, T maximumValue)
	{
		super(name, defaultValue);
		this.minimumValue = minimumValue;
		this.maximumValue = maximumValue;
	}

	@Override
	public T getMinimumValue()
	{
		return minimumValue;
	}

	@Override
	public T getMaximumValue()
	{
		return maximumValue;
	}
}