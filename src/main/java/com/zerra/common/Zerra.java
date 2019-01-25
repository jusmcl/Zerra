package com.zerra.common;

import com.zerra.common.event.EventHandler;
import com.zerra.common.network.ConnectionManager;
import com.zerra.common.network.Message;
import com.zerra.common.util.Timer;
import com.zerra.common.world.World;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Zerra implements Runnable
{
	protected static final Logger LOGGER = LogManager.getLogger(Reference.NAME);

	protected ExecutorService pool;
	protected ExecutorService single;
	protected boolean running;

	protected Timer timer;
	protected World world;

	protected EventHandler eventHandler;

	public Zerra()
	{
		this.pool = Executors.newCachedThreadPool();
		this.single = Executors.newSingleThreadExecutor(r -> new Thread(r, "Message Processing"));
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

	protected void init()
	{
		this.start();
	}

	public abstract boolean isClient();

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

	/**
	 * Schedules the handling of the message on a single thread executor
	 *
	 * @param message {@link Message} to be handled
	 */
	public void handleMessage(Message message)
	{
		single.execute(() ->
		{
			//Validate side message is meant for
			if (!message.getReceivingSide().isValidForSide(this.isClient()))
			{
				logger().warn("Message {} was sent to {} even though its receiving side is {}!",
					message.getClass().getName(), this.isClient() ? "client" : "server", message.getReceivingSide());
				return;
			}
			Message m = message.handle(this, getWorld());
			//If returned a new message, send it back to the sender
			if (m != null)
			{
				if (!message.includesSender() && !this.isClient())
				{
					logger().warn("Message of type {} sent from client wants to reply from server, but no sender UUID was included!", message.getClass().getName());
				}
				else
				{
					if (message.getSender() == null)
					{
						//Server doesn't have a sender UUID
						getConnectionManager().sendToServer(m);
					}
					else
					{
						getConnectionManager().sendToClient(m, message.getSender());
					}
				}
			}
		});
	}

	public abstract ConnectionManager getConnectionManager();
}
