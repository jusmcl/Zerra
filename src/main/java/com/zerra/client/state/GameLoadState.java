package com.zerra.client.state;

import com.zerra.client.presence.PresenceBuilder;
import com.zerra.client.state.TexturePreloader.LoadingState;

public class GameLoadState extends State
{

	public GameLoadState()
	{
		super("gameload");
		new Thread(new TexturePreloader()).run();
	}
	
	@Override
	public PresenceBuilder setupPresence()
	{
		return new PresenceBuilder().setDetails("Loading Game").setLargeImage("zerra");
	}

	@Override
	public void update()
	{
		if (TexturePreloader.getState() == LoadingState.Done)
		{
			StateManager.setActiveState(new MenuState());
			TexturePreloader.setState(LoadingState.None);
		}
	}

	@Override
	public void render(double mouseX, double mouseY, float partialTicks)
	{

	}
}
