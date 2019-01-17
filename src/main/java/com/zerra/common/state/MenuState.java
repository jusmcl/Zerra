package com.zerra.common.state;

import com.zerra.client.Zerra;

public class MenuState extends State
{

	private Zerra zerra;

	public MenuState()
	{
		super("menu");
		zerra = Zerra.getInstance();
		zerra.getPresence().setPresence("In Menu", "512x512", "none");
	}
	
	@Override
	public void update()
	{
		Zerra.getInstance().getCamera().update();
		Zerra.getInstance().getInputHandler().updateGamepad();
	}

	@Override
	public void render()
	{
		// TODO: Render a background and buttons here!
	}
}
