package com.zerra.common.state;

import com.zerra.client.Zerra;

public class State
{

	private String name;
	
	protected Zerra zerra;

	public State(String name)
	{
		this.name = name;
		this.zerra = Zerra.getInstance();
		Zerra.logger().info("Loading " + this.getName() + " state.");
	}

	// No rendering can ever be done in this method
	public void update()
	{

	}

	public void render()
	{
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
