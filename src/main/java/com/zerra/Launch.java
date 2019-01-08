package com.zerra;

import com.zerra.client.Zerra;
import com.zerra.common.ArgsBuilder;
import com.zerra.server.ZerraServer;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em>
 * 
 * <br>
 * </br>
 * 
 * The main starting point for the game.
 * 
 */
public class Launch {

	public static final String NAME = "Zerra";
	public static final String VERSION = "0.0.3";
	public static final String DOMAIN = "zerra";
	public static final boolean IS_DEVELOPMENT_BUILD = true;

	public static void main(String[] args) {
		ArgsBuilder builder = ArgsBuilder.deserialize(args);
		if (builder.isServer()) {
			new Thread(new ZerraServer(), "server").start();
		} else {
			new Thread(new Zerra(), "main").start();
		}

		DiscordRPC lib = DiscordRPC.INSTANCE;
		String applicationId = "532051034453966858";

		DiscordEventHandlers handlers = new DiscordEventHandlers();

		handlers.ready = (user) -> System.out.println("Ready!");

		lib.Discord_Initialize(applicationId, handlers, true, null);

		DiscordRichPresence presence = new DiscordRichPresence();

		presence.startTimestamp = System.currentTimeMillis() / 1000; // epoch second
		presence.details = "Playing Zerra";
		lib.Discord_UpdatePresence(presence);

		// in a worker thread
		new Thread(() ->
		{
			while (!Thread.currentThread().isInterrupted() && Zerra.getInstance() != null)
			{
				lib.Discord_RunCallbacks();
				try
				{
					Thread.sleep(2000);
				} catch (InterruptedException ignored)
				{
				}
			}
		}, "RPC-Callback-Handler").start();
	}
}