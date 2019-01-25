package com.zerra.client.state;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.zerra.client.ZerraClient;

public class TexturePreloader implements Runnable
{

	public enum LoadingState
	{
		None,
		Loading,
		Done
	}

	private static LoadingState currentLoadingState = LoadingState.None;
	private final static Object threadLock = new Object();

	public static void setState(LoadingState state)
	{
		synchronized (threadLock)
		{
			currentLoadingState = state;
		}
	}

	public static LoadingState getState()
	{
		synchronized (threadLock)
		{
			return currentLoadingState;
		}
	}

	private static final Logger LOGGER = LogManager.getLogger("Texture Loader");

	@Override
	public void run()
	{
		setState(LoadingState.Loading);
		// TODO: Use log4j logger without errors
		System.out.println("Started loading textures");
		// TODO: Load textures
		ZerraClient.getInstance().getRenderingManager().getTextureManager().registerTiles();
		ZerraClient.getInstance().getRenderingManager().getTextureManager().getTextureMap().stitch();
		System.out.println("Done loading textures");
		setState(LoadingState.Done);
	}

}
