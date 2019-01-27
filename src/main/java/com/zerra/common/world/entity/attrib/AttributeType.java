package com.zerra.common.world.entity.attrib;

import java.util.Map;

import com.google.common.collect.Maps;

public enum AttributeType
{
	BYTE, SHORT, INTEGER, FLOAT, DOUBLE, LONG, BYTE_RANGE, SHORT_RANGE, INTEGER_RANGE, FLOAT_RANGE, DOUBLE_RANGE, LONG_RANGE, BOOLEAN, STRING;

	private static final Map<Byte, AttributeType> TYPES_LOOKUP = Maps.<Byte, AttributeType>newHashMap();

	public byte getId()
	{
		return (byte) this.ordinal();
	}

	public boolean isNumber()
	{
		return this == BYTE || this == SHORT || this == INTEGER || this == FLOAT || this == DOUBLE || this == LONG || this == BYTE_RANGE || this == SHORT_RANGE || this == INTEGER_RANGE || this == FLOAT_RANGE || this == DOUBLE_RANGE || this == LONG_RANGE;
	}

	public boolean isRange()
	{
		return this == BYTE_RANGE || this == SHORT_RANGE || this == INTEGER_RANGE || this == FLOAT_RANGE || this == DOUBLE_RANGE || this == LONG_RANGE;
	}

	public static AttributeType byId(int id)
	{
		return TYPES_LOOKUP.get((byte) id);
	}

	static
	{
		for (AttributeType type : values())
		{
			TYPES_LOOKUP.put(type.getId(), type);
		}
	}
}