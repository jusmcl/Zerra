package com.zerra.client.gfx;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Light {

	private Vector2f position;
	private Vector3f color;
	private float brightness;

	public Light(Vector2f position, float red, float green, float blue, float brightness) {
		this.position = new Vector2f(position);
		this.color = new Vector3f(red, green, blue);
		this.brightness = brightness;
	}

	public Vector2f getPosition() {
		return position;
	}

	public Vector3f getColor() {
		return color;
	}

	public float getBrightness() {
		return brightness;
	}
}