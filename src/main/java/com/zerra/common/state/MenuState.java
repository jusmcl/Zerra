package com.zerra.common.state;

import com.zerra.client.ZerraClient;

public class MenuState extends State
{

	private ZerraClient zerra;

	public MenuState()
	{
		super("menu");
		zerra = ZerraClient.getInstance();
	}
	
	@Override
	public void update()
	{
		ZerraClient.getInstance().getCamera().update();
		ZerraClient.getInstance().getInputHandler().updateGamepad();
	}

	@Override
	public void render()
	{
		// TODO: Render a background and buttons here!
	}
}
