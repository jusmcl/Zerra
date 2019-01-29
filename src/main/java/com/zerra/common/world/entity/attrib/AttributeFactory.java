package com.zerra.common.world.entity.attrib;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em>
 * 
 * <br>
 * </br>
 * 
 * The attribute factory creates new {@link Attribute}s for different types of values to be stored.
 * 
 * @author Ocelot5836
 */
public class AttributeFactory
{
	/**
	 * Creates a new attribute for a number.
	 * 
	 * @param name
	 *            The name of the attribute
	 * @param defaultValue
	 *            The default value of the attribute
	 * @return The attribute created for type {@link Number}
	 * @see Attribute
	 */
	public static <T extends Number> Attribute<T> createNumberAttribute(String name, T defaultValue)
	{
		return new NumberAttribute<T>(name, defaultValue);
	}

	/**
	 * Creates a new attribute for a number range.
	 * 
	 * @param name
	 *            The name of the attribute
	 * @param defaultValue
	 *            The default value of the attribute
	 * @param minimumValue
	 *            The minimumValue of the attribute
	 * @param maximumValue
	 *            The maximumValue of the attribute
	 * @return The attribute created for type {@link Number}
	 * @see RangeAttribute
	 */
	public static <T extends Number> RangeAttribute<T> createNumberRangeAttribute(String name, T defaultValue, T minimumValue, T maximumValue)
	{
		return new NumberRangeAttribute<T>(name, defaultValue, minimumValue, maximumValue);
	}

	/**
	 * Creates a new attribute for a boolean.
	 * 
	 * @param name
	 *            The name of the attribute
	 * @param defaultValue
	 *            The default value of the attribute
	 * @return The attribute created for type {@link Boolean}
	 * @see Attribute
	 */
	public static Attribute<Boolean> createBooleanAttribute(String name, boolean defaultValue)
	{
		return new BooleanAttribute(name, defaultValue);
	}

	/**
	 * Creates a new attribute for a string.
	 * 
	 * @param name
	 *            The name of the attribute
	 * @param defaultValue
	 *            The default value of the attribute
	 * @return The attribute created for type {@link String}
	 * @see Attribute
	 */
	public static Attribute<String> createStringAttribute(String name, String defaultValue)
	{
		return new StringAttribute(name, defaultValue);
	}
}