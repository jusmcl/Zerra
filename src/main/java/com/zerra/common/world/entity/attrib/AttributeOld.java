package com.zerra.common.world.entity.attrib;

//TODO move these into some entity class
@Deprecated
public enum AttributeOld
{
	HEALTH(0, 50, 50), STAMINA(10), MANA(0, 50, 50), PHYS_ARMOR(-100, 0, 90), SPELL_ARMOR(-100, 0, 90);

	private int value;
	private int minValue;
	private int maxValue;

	AttributeOld(int value)
	{
		this.value = value;
	}

	AttributeOld(int minValue, int value, int maxValue)
	{
		this.minValue = minValue;
		this.value = value;
		this.maxValue = maxValue;
	}

	public int getMinValue()
	{
		return this.minValue;
	}

	public void setMinValue(int minValue)
	{
		this.minValue = minValue;
	}

	public int getValue()
	{
		return this.value;
	}

	public void setValue(int value)
	{
		this.value = value;
	}

	public int getMaxValue()
	{
		return this.maxValue;
	}

	public void setMaxValue(int maxValue)
	{
		this.maxValue = maxValue;
	}
}
