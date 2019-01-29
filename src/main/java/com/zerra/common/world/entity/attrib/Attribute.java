package com.zerra.common.world.entity.attrib;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em>
 * 
 * <br>
 * </br>
 * 
 * An attribute is a simple way to store a value in an entity easily.
 * 
 * @author Ocelot5836
 *
 * @param <T>
 *            The type of value this attribute stores
 */
public interface Attribute<T>
{
	/**
	 * @return The name of this attribute
	 */
	String getName();
	
	/**
	 * @return The default value of this attribute
	 */
	T getDefaultValue();
}