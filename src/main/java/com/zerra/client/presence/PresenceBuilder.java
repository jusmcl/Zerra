package com.zerra.client.presence;

import com.zerra.client.ZerraClient;

public class PresenceBuilder
{

	public PresenceBuilder setDetails(String details)
	{
		ZerraClient.getInstance().getPresenceManager().getPresence().details = details;
		return this;
	}

	public PresenceBuilder setLargeImage(String key)
	{
		ZerraClient.getInstance().getPresenceManager().getPresence().largeImageKey = key;
		return this;
	}

	public PresenceBuilder setLargeImageText(String text)
	{
		ZerraClient.getInstance().getPresenceManager().getPresence().largeImageText = text;
		return this;
	}

	public void build()
	{
		ZerraClient.getInstance().getPresenceManager().getPresence().startTimestamp = System.currentTimeMillis() / 1000;
		ZerraClient.getInstance().getPresenceManager().updatePresence();
	}
}
