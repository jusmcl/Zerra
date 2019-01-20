package com.zerra.client.gfx;

import org.joml.Vector3f;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * A standard point-light that exists in the world.
 * 
 * @author Ocelot5836
 */
public class Light
{

	private Vector3f position;
	private Vector3f color;
	private float brightness;

	public Light(Vector3f position, float red, float green, float blue, float brightness)
	{
		this.position = new Vector3f(position);
		this.color = new Vector3f(red, green, blue);
		this.brightness = brightness;
	}

	/**
	 * @return The position of the light
	 */
	public Vector3f getPosition()
	{
		return position;
	}

	/**
	 * @return The color of the light
	 */
	public Vector3f getColor()
	{
		return color;
	}

	/**
	 * @return The brightness of the light
	 */
	public float getBrightness()
	{
		return brightness;
	}
}