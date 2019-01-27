package com.zerra.common.world.entity.attrib;

import com.zerra.common.world.storage.Storable;

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
	 * Creates a new attribute for a number and sets the value.
	 * 
	 * @param defaultValue
	 *            The default value of the attribute
	 * @param value
	 *            The new value of the attribute
	 * @return The attribute created for type {@link Number}
	 * @see Attribute
	 */
	public static Attribute<Number> createNumberAttribute(Number defaultValue, Number value)
	{
		Attribute<Number> attribute = new NumberAttribute(defaultValue);
		attribute.setValue(value);
		return attribute;
	}

	/**
	 * Creates a new attribute for a number.
	 * 
	 * @param defaultValue
	 *            The default value of the attribute
	 * @return The attribute created for type {@link Number}
	 * @see Attribute
	 */
	public static Attribute<Number> createNumberAttribute(Number defaultValue)
	{
		return new NumberAttribute(defaultValue);
	}

	/**
	 * Creates a new attribute for a number range and sets the value.
	 * 
	 * @param defaultValue
	 *            The default value of the attribute
	 * @param value
	 *            The new value of the attribute
	 * @param minimumValue
	 *            The minimumValue of the attribute
	 * @param maximumValue
	 *            The maximumValue of the attribute
	 * @return The attribute created for type {@link Number}
	 * @see RangeAttribute
	 */
	public static RangeAttribute<Number> createNumberRangeAttribute(Number defaultValue, Number value, Number minimumValue, Number maximumValue)
	{
		RangeAttribute<Number> attribute = new NumberRangeAttribute(defaultValue, minimumValue, maximumValue);
		attribute.setValue(value);
		return attribute;
	}

	/**
	 * Creates a new attribute for a number range.
	 * 
	 * @param defaultValue
	 *            The default value of the attribute
	 * @param minimumValue
	 *            The minimumValue of the attribute
	 * @param maximumValue
	 *            The maximumValue of the attribute
	 * @return The attribute created for type {@link Number}
	 * @see RangeAttribute
	 */
	public static RangeAttribute<Number> createNumberRangeAttribute(Number defaultValue, Number minimumValue, Number maximumValue)
	{
		return new NumberRangeAttribute(defaultValue, minimumValue, maximumValue);
	}

	/**
	 * Creates a new attribute for a boolean and sets the value.
	 * 
	 * @param defaultValue
	 *            The default value of the attribute
	 * @param value
	 *            The new value of the attribute
	 * @return The attribute created for type {@link Boolean}
	 * @see Attribute
	 */
	public static Attribute<Boolean> createBooleanAttribute(boolean defaultValue, boolean value)
	{
		Attribute<Boolean> attribute = new BooleanAttribute(defaultValue);
		attribute.setValue(value);
		return attribute;
	}

	/**
	 * Creates a new attribute for a boolean.
	 * 
	 * @param defaultValue
	 *            The default value of the attribute
	 * @return The attribute created for type {@link Boolean}
	 * @see Attribute
	 */
	public static Attribute<Boolean> createBooleanAttribute(boolean defaultValue)
	{
		return new BooleanAttribute(defaultValue);
	}

	/**
	 * Creates a new attribute for a string and sets the value.
	 * 
	 * @param defaultValue
	 *            The default value of the attribute
	 * @param value
	 *            The new value of the attribute
	 * @return The attribute created for type {@link String}
	 * @see Attribute
	 */
	public static Attribute<String> createStringAttribute(String defaultValue, String value)
	{
		Attribute<String> attribute = new StringAttribute(defaultValue);
		attribute.setValue(value);
		return attribute;
	}

	/**
	 * Creates a new attribute for a string.
	 * 
	 * @param defaultValue
	 *            The default value of the attribute
	 * @return The attribute created for type {@link String}
	 * @see Attribute
	 */
	public static Attribute<String> createStringAttribute(String defaultValue)
	{
		return new StringAttribute(defaultValue);
	}

	/**
	 * Creates a new attribute for a storable object and sets the value.
	 * 
	 * @param defaultValue
	 *            The default value of the attribute
	 * @param value
	 *            The new value of the attribute
	 * @return The attribute created for type {@link Storable}
	 * @see Attribute
	 */
	public static Attribute<Storable> createObjectAttribute(Storable defaultValue, Storable value)
	{
		Attribute<Storable> attribute = new ObjectAttribute(defaultValue);
		attribute.setValue(value);
		return attribute;
	}

	/**
	 * Creates a new attribute for a storable object.
	 * 
	 * @param defaultValue
	 *            The default value of the attribute
	 * @return The attribute created for type {@link Storable}
	 * @see Attribute
	 */
	public static Attribute<Storable> createObjectAttribute(Storable defaultValue)
	{
		return new ObjectAttribute(defaultValue);
	}
}