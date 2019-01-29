package com.zerra.client.state;

import com.zerra.client.presence.PresenceBuilder;

public class WorldLoadState extends State
{

	public WorldLoadState()
	{
		super("worldload");
	}

	@Override
	public PresenceBuilder setupPresence()
	{
		return new PresenceBuilder().setDetails("Loading World").setLargeImage("zerra");
	}

	@Override
	public void update()
	{
		// TODO: Update the loading bar for the world?
	}

	@Override
	public void render(double mouseX, double mouseY, float partialTicks)
	{
		// TODO: World loading screen.
	}
}
