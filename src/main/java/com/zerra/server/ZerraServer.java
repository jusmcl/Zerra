package com.zerra.server;

import java.util.concurrent.Executors;

import com.zerra.common.Zerra;
import com.zerra.common.ZerraContentInit;
import com.zerra.common.event.EventHandler;
import com.zerra.common.network.ConnectionManager;
import com.zerra.common.util.Timer;
import com.zerra.common.world.storage.Layer;
import com.zerra.common.world.tile.Tiles;
import com.zerra.server.network.ServerConnectionManager;
import com.zerra.server.world.ServerWorld;
import org.joml.Vector3i;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * The server for the game.
 * 
 * @author Arpaesis
 */
public class ZerraServer extends Zerra
{
	private static ZerraServer instance;

	private boolean isNaturallyRemote;
	private boolean isCurrentlyRemote;

	private String address;

	private ServerConnectionManager serverManager;
  
	private ServerWorld world;

	public ZerraServer(boolean isNaturallyRemote)
	{
		this(isNaturallyRemote, ConnectionManager.LOCALHOST);
	}

	public ZerraServer(boolean isNaturallyRemote, String address)
	{
		super();
		instance = this;

		this.address = address;

		this.isNaturallyRemote = isNaturallyRemote;
		this.isCurrentlyRemote = false;
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
		this.world.update();
	}

	@Override
	protected void init()
	{
		super.init();

		//Check if this is remote, as we don't want to register everything twice if running integrated
		if (this.isCurrentlyRemote)
		{
			ZerraContentInit.init();
			//TODO: Move tiles registration into ZerraContentInit
			Tiles.registerTiles();
		}

		this.timer = new Timer(20);

		this.world = new ServerWorld("world");

		this.serverManager = new ServerConnectionManager(this, this.isNaturallyRemote ? address : null);

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

	@Override
	public boolean isClient()
	{
		return false;
	}

	public static ZerraServer getInstance()
	{
		return instance;
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

	@Override
	public ServerConnectionManager getConnectionManager()
	{
		return serverManager;
	}

	public ServerWorld getWorld()
	{
		return this.world;
	}
}