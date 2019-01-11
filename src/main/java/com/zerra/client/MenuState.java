package com.zerra.client;

public class MenuState extends State
{

	@Override
	public void update()
	{
		Zerra.getInstance().camera.update();
		Zerra.getInstance().inputHandler.updateGamepad();
	}

	@Override
	public void render()
	{
		// TODO: Render a background and buttons here!
	}
}
