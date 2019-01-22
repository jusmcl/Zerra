package com.zerra.client.state;

import com.zerra.client.ZerraClient;

public class MenuState extends State
{

	public MenuState()
	{
		super("menu");
	}

	@Override
	public void update()
	{
		ZerraClient.getInstance().getRenderingManager().getCamera().update();
		ZerraClient.getInstance().getInputHandler().updateGamepad();
	}

	@Override
	public void render()
	{
		// TODO: Render a background and buttons here!
	}
}
