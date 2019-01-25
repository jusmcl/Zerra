package com.zerra.common.network;

/**
 * Used to represent the side which the message is meant to be received by
 */
public enum MessageSide
{
	CLIENT(true, false),
	SERVER(false, true),
	BOTH(true, true);

	private final boolean client, server;

	MessageSide(boolean client, boolean server)
	{
		this.client = client;
		this.server = server;
	}

	public boolean isValidForSide(boolean isClient)
	{
		return isClient ? client : server;
	}

	public boolean isForClient()
	{
		return client;
	}

	public boolean isForServer()
	{
		return server;
	}
}
