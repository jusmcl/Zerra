package com.zerra.server;

import com.zerra.common.Zerra;
import com.zerra.common.event.EventHandler;
import com.zerra.common.network.ConnectionManager;
import com.zerra.common.util.Timer;
import com.zerra.common.world.storage.Layer;
import com.zerra.server.network.ServerConnectionManager;
import com.zerra.server.world.ServerWorld;
import org.joml.Vector3i;

import java.util.concurrent.Executors;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * The server for the game.
 * 
 * @author Arpaesis
 */
public class ZerraServer extends Zerra
{

	private boolean isNaturallyRemote;
	private boolean isCurrentlyRemote;

	private String address;

	private ServerConnectionManager serverManager;

	public ZerraServer(boolean isNaturallyRemote)
	{
		this(isNaturallyRemote, ConnectionManager.LOCALHOST);
	}

	public ZerraServer(boolean isNaturallyRemote, String address)
	{
		super();
		instance = this;
		this.pool = Executors.newCachedThreadPool();

		this.address = address;

		this.isNaturallyRemote = isNaturallyRemote;
		this.isCurrentlyRemote = false;

		serverManager = new ServerConnectionManager();
	}

	/**
	 * Sets the game's running status to true.
	 */
	@Override
	public synchronized void start()
	{
		if (this.running)
			return;

		LOGGER.info("Starting...");
		this.running = true;
	}

	/**
	 * Sets the game's running status to false.
	 */
	@Override
	public synchronized void stop()
	{
		if (!this.running)
			return;

		LOGGER.info("Stopping...");
		this.running = false;
		this.world.stop();
		this.serverManager.close();
	}

	// TODO improve loop
	@Override
	public void run()
	{
		this.init();

		while (this.running)
		{
			this.timer.updateTimer();

			for (int i = 0; i < Math.min(10, this.timer.elapsedTicks); ++i)
			{
				update();
			}
		}
	}

	private void update()
	{
		// Update server world here.
	}

	@Override
	protected void init()
	{
		super.init();
		this.timer = new Timer(20);

		this.world = new ServerWorld("world");

		this.serverManager = this.isNaturallyRemote ? new ServerConnectionManager(address) : new ServerConnectionManager();
		this.serverManager.setWorld(this.world);

		Layer layer = world.getLayer(0);
		for (int x = 0; x < 3; x++)
		{
			for (int z = 0; z < 3; z++)
			{
				layer.loadPlate(new Vector3i(x - 1, 0, z - 1));
			}
		}
		this.eventHandler = new EventHandler();
	}

	public static ZerraServer getInstance()
	{
		return (ZerraServer) instance;
	}

	public boolean isNaturallyRemote()
	{
		return isNaturallyRemote;
	}

	public boolean isCurrentlyRemote()
	{
		return isCurrentlyRemote;
	}

	public void setCurrentlyRemote(boolean isCurrentlyRemote)
	{
		this.isCurrentlyRemote = isCurrentlyRemote;
	}

	public ServerConnectionManager getServerManager()
	{
		return this.serverManager;
	}
}