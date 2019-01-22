package com.zerra.client.state;

public class LoadingManager
{

	public enum LoadingState
	{
		None,
		WorldPlates,
		GenerateMesh,
		JoinServer
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

}
