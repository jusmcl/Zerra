package com.zerra.common.world.entity.attrib;

import java.util.Map;

import com.google.common.collect.Maps;

public enum AttributeType
{
	BYTE, SHORT, INTEGER, FLOAT, DOUBLE, LONG, BOOLEAN, STRING;

	private static final Map<Byte, AttributeType> TYPES_LOOKUP = Maps.<Byte, AttributeType>newHashMap();

	public byte getId()
	{
		return (byte) this.ordinal();
	}

	public boolean isNumber()
	{
		return this == BYTE || this == SHORT || this == INTEGER || this == FLOAT || this == DOUBLE || this == LONG;
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