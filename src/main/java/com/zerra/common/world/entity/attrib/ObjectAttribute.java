package com.zerra.common.world.entity.attrib;

import com.zerra.common.world.storage.Storable;

public class ObjectAttribute implements Attribute<Storable>
{
	private Storable defaultValue;
	private Storable value;

	protected ObjectAttribute(Storable defaultValue)
	{
		this.defaultValue = defaultValue;
		this.value = defaultValue;
	}

	@Override
	public Storable getDefaultValue()
	{
		return defaultValue;
	}

	@Override
	public Storable getValue()
	{
		return value;
	}

	@Override
	public void setValue(Storable value)
	{
		this.value = value;
	}
}