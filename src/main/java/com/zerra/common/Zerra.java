package com.zerra.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zerra.common.event.EventHandler;
import com.zerra.common.util.Timer;
import com.zerra.server.network.ServerConnectionManager;

public class Zerra implements Runnable
{
	protected static final Logger LOGGER = LogManager.getLogger(Reference.NAME);

	protected static Zerra instance;

	protected ExecutorService pool;
	protected boolean running;

	protected Timer timer;

	protected EventHandler eventHandler;

	protected ServerConnectionManager serverManager;

	public Zerra()
	{
		this.pool = Executors.newCachedThreadPool();

	}

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

	public EventHandler getEventHandler()
	{
		return eventHandler;
	}

	public static Logger logger()
	{
		return LOGGER;
	}

	/**
	 * Schedules a new task.
	 * 
	 * @param runnable - The task to schedule.
	 */
	public void schedule(Runnable runnable)
	{
		Validate.notNull(runnable);
		this.pool.execute(runnable);
	}
}
