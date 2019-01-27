package com.zerra.common.world.entity.attrib;

public class StringAttribute implements Attribute<String>
{
	private String defaultValue;
	private String value;

	protected StringAttribute(String defaultValue)
	{
		this.defaultValue = defaultValue;
		this.value = defaultValue;
	}

	@Override
	public String getDefaultValue()
	{
		return defaultValue;
	}

	@Override
	public String getValue()
	{
		return value;
	}

	@Override
	public void setValue(String value)
	{
		this.value = value;
	}
}