package com.zerra.common.world.entity.attrib;

public class NumberRangeAttribute<T extends Number> extends NumberAttribute<T> implements RangeAttribute<T>
{
	private T minimumValue;
	private T maximumValue;

	protected NumberRangeAttribute(T defaultValue, T minimumValue, T maximumValue)
	{
		super(defaultValue);
		this.minimumValue = minimumValue;
		this.maximumValue = maximumValue;
	}
	
	@Override
	public void setValue(T value)
	{
		if(value.doubleValue() < this.minimumValue.doubleValue())
			value = this.minimumValue;
		if(value.doubleValue() > this.maximumValue.doubleValue())
			value = this.maximumValue;
		super.setValue(value);
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

	@Override
	public void setMinimumValue(T minimumValue)
	{
		this.minimumValue = minimumValue;
	}

	@Override
	public void setMaximumValue(T maximumValue)
	{
		this.maximumValue = maximumValue;
	}
}