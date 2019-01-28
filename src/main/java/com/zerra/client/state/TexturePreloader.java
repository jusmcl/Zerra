package com.zerra.client.state;

import com.zerra.client.ZerraClient;
import com.zerra.common.Zerra;
import com.zerra.common.ZerraContentInit;

public class TexturePreloader implements Runnable
{

	public enum LoadingState
	{
		None, Loading, Done
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

	@Override
	public void run()
	{
		setState(LoadingState.Loading);
		Zerra.logger().info("Started loading textures");
		// TODO: Load textures
		ZerraContentInit.registerTextureMapSprites(ZerraClient.getInstance().getRenderingManager().getTextureManager().getTextureMap());
		ZerraClient.getInstance().getRenderingManager().getTextureManager().getTextureMap().stitch();
		ZerraContentInit.initClient();
		Zerra.logger().info("Done loading textures");
		setState(LoadingState.Done);
	}

}
