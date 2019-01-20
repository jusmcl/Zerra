package com.zerra.client.input.gamepad;

public class Joystick
{

	private float lastX;
	private float x;
	private float lastY;
	private float y;

	protected Joystick()
	{
		this.lastX = 0;
		this.x = 0;
		this.lastY = 0;
		this.y = 0;
	}

	protected void updateAxes(float x, float y)
	{
		this.lastX = this.x;
		this.lastY = this.y;
		this.x = x;
		this.y = y;
	}

	public float getXDirection()
	{
		return this.x - this.lastX;
	}

	public float getYDirection()
	{
		return this.y - this.lastY;
	}

	public float getLastX()
	{
		return Math.abs(this.lastX) < 0.01 ? 0 : this.lastX;
	}

	public float getX()
	{
		return Math.abs(this.x) < 0.01 ? 0 : this.x;
	}

	public float getLastY()
	{
		return Math.abs(this.lastY) < 0.01 ? 0 : this.lastY;
	}

	public float getY()
	{
		return Math.abs(this.y) < 0.01 ? 0 : this.y;
	}
}