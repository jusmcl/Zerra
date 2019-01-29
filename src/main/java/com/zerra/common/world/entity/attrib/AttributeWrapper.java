package com.zerra.common.world.entity.attrib;

public class AttributeWrapper<T>
{
	private Attribute<T> attribute;
	private T value;

	public AttributeWrapper(Attribute<T> attribute, T value)
	{
		this.attribute = attribute;
		this.value = value;
	}
	
	/**
	 * @return The default value of this attribute
	 */
	public T getDefaultValue() {
		return attribute.getDefaultValue();
	}

	/**
	 * @return The value this attribute is set to
	 */
	public T getValue()
	{
		return value;
	}

	/**
	 * Sets the value to the value specified.
	 * 
	 * @param value
	 *            The new value
	 */
	public void setValue(T value)
	{
		this.value = value;
	}
}