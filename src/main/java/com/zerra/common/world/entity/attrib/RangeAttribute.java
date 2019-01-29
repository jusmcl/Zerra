package com.zerra.common.world.entity.attrib;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em>
 * 
 * <br>
 * </br>
 * 
 * A range attribute is a simple way to store a range of values in an entity easily.
 * 
 * @author Ocelot5836
 *
 * @param <T>
 *            The type of value this attribute stores
 */
public interface RangeAttribute<T> extends Attribute<T>
{
	/**
	 * @return The minimum value of this attribute
	 */
	T getMinimumValue();

	/**
	 * @return The maximum value of this attribute
	 */
	T getMaximumValue();
}