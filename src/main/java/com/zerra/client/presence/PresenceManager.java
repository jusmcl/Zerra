package com.zerra.client.presence;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class PresenceManager
{

	private DiscordRichPresence presence;
	private DiscordRPC lib;
	private boolean running = true;

	public PresenceManager()
	{
		this.presence = new DiscordRichPresence();
		init();
	}

	public void init()
	{
		lib = DiscordRPC.INSTANCE;
		String applicationId = "539622563622551554";
		DiscordEventHandlers handlers = new DiscordEventHandlers();
		lib.Discord_Initialize(applicationId, handlers, true, null);
		lib.Discord_UpdatePresence(presence);
		// in a worker thread
		new Thread(() ->
		{
			while (running)
			{
				lib.Discord_RunCallbacks();
				try
				{
					Thread.sleep(2000);
				} catch (InterruptedException ignored)
				{
				}
			}
		}, "Discord-Rich-Presence").start();
	}

	public DiscordRichPresence getPresence()
	{
		return presence;
	}

	public void updatePresence()
	{
		lib.Discord_UpdatePresence(presence);
	}

	public void setPresence(PresenceBuilder builder)
	{
		builder.build();
	}

	public void stop()
	{
		running = false;
	}
}
