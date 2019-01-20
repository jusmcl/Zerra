package com.zerra.common;

import java.util.concurrent.ExecutorService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zerra.ClientLaunch;
import com.zerra.client.util.Timer;
import com.zerra.common.event.EventHandler;
import com.zerra.common.world.World;
import com.zerra.server.network.ServerManager;

public class Zerra implements Runnable
{
	protected static final Logger LOGGER = LogManager.getLogger(ClientLaunch.NAME);

	protected static Zerra instance;

	protected ExecutorService pool;
	protected boolean running;
	protected boolean serverReady = false;

	protected Timer timer;
	protected World world;

	protected EventHandler eventHandler;

	protected ServerManager server;

	public void run()
	{
	}

	public synchronized void start()
	{
	}

	public synchronized void stop()
	{
	}

	protected void init() throws Throwable
	{
	}

	public World getWorld()
	{
		return world;
	}

	public EventHandler getEventHandler()
	{
		return eventHandler;
	}

	public static Logger logger()
	{
		return LOGGER;
	}
}
