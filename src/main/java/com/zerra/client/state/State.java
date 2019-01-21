package com.zerra.client.state;

import com.zerra.client.ZerraClient;
import com.zerra.server.ZerraServer;

public abstract class State
{

	private String name;

	protected ZerraClient zerraClient;
	protected ZerraServer zerraServer;

	public State(String name)
	{
		this.name = name;
		this.zerraClient = ZerraClient.getInstance();
		reloadServerInstance();
		ZerraClient.logger().info("Loading " + this.getName() + " state.");
	}
	
	public void reloadServerInstance() {
		this.zerraServer = ZerraServer.getInstance();
	}

	// No rendering can ever be done in this method
	public abstract void update();

	public abstract void render();

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
