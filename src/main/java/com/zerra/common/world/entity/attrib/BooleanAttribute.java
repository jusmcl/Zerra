package com.zerra.common.world.entity.attrib;

public class BooleanAttribute implements Attribute<Boolean>
{
	private boolean defaultValue;
	private boolean value;

	protected BooleanAttribute(boolean defaultValue)
	{
		this.defaultValue = defaultValue;
		this.value = defaultValue;
	}

	@Override
	public Boolean getDefaultValue()
	{
		return defaultValue;
	}

	@Override
	public Boolean getValue()
	{
		return value;
	}

	@Override
	public void setValue(Boolean value)
	{
		this.value = value;
	}
}