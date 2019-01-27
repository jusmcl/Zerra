package com.zerra.common.world.entity.attrib;

public class NumberRangeAttribute extends NumberAttribute implements RangeAttribute<Number>
{
	private Number minimumValue;
	private Number maximumValue;

	protected NumberRangeAttribute(Number defaultValue, Number minimumValue, Number maximumValue)
	{
		super(defaultValue);
		this.minimumValue = minimumValue;
		this.maximumValue = maximumValue;
	}
	
	@Override
	public void setValue(Number value)
	{
		if(value.doubleValue() < this.minimumValue.doubleValue())
			value = this.minimumValue;
		if(value.doubleValue() > this.maximumValue.doubleValue())
			value = this.maximumValue;
		super.setValue(value);
	}

	@Override
	public Number getMinimumValue()
	{
		return minimumValue;
	}

	@Override
	public Number getMaximumValue()
	{
		return maximumValue;
	}

	@Override
	public void setMinimumValue(Number minimumValue)
	{
		this.minimumValue = minimumValue;
	}

	@Override
	public void setMaximumValue(Number maximumValue)
	{
		this.maximumValue = maximumValue;
	}
}