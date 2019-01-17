package com.zerra.client.state;

import com.zerra.client.ZerraClient;

public class WorldLoadState extends State
{

	private ZerraClient zerra;

	public WorldLoadState()
	{
		super("worldload");
		zerra = ZerraClient.getInstance();
		zerra.getPresence().setPresence("Loading World", "512x512", "none");
	}

	@Override
	public void update()
	{
		// TODO: Update the loading bar for the world?
	}

	@Override
	public void render()
	{
		// TODO: World loading screen.
	}
}
