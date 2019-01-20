package com.zerra.client.state;

import com.zerra.client.ZerraClient;
import com.zerra.common.Zerra;

public class State
{

	private String name;

	protected Zerra zerra;

	public State(String name)
	{
		this.name = name;
		// TODO: Make sure this should use ZerraClient, and not ZerraServer.
		this.zerra = ZerraClient.getInstance();
		ZerraClient.logger().info("Loading " + this.getName() + " state.");
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
