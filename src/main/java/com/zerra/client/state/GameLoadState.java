package com.zerra.client.state;

import com.zerra.client.state.TexturePreloader.LoadingState;

public class GameLoadState extends State
{

	public GameLoadState()
	{
		super("gameload");
		new Thread(new TexturePreloader()).run();
	}

	@Override
	public void update()
	{
		if (TexturePreloader.getState() == LoadingState.Done)
		{
			StateManager.setActiveState(new WorldState());
			TexturePreloader.setState(LoadingState.None);
		}
	}

	@Override
	public void render()
	{
		
	}
}
