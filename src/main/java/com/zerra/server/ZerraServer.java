package com.zerra.server;

import java.util.concurrent.Executors;

import org.apache.commons.lang3.Validate;
import org.joml.Vector3i;

import com.zerra.client.state.StateManager;
import com.zerra.client.util.Timer;
import com.zerra.common.Zerra;
import com.zerra.common.event.EventHandler;
import com.zerra.common.world.World;
import com.zerra.common.world.storage.Layer;
import com.zerra.server.network.ServerManager;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em>
 * 
 * <br>
 * </br>
 * 
 * The server for the game.
 * 
 * @author Arpaesis
 */
public class ZerraServer extends Zerra {

	private boolean isNaturallyRemote;
	private boolean isCurrentlyRemote;
	
	public ZerraServer(boolean isNaturallyRemote) {
		instance = this;
		this.pool = Executors.newCachedThreadPool();
		
		this.isNaturallyRemote = isNaturallyRemote;
		this.isCurrentlyRemote = false;
		
		server = new ServerManager();
		
		this.start();
	}

	/**
	 * Sets the game's running status to true.
	 */
	@Override
	public synchronized void start() {
		if (this.running)
			return;

		LOGGER.info("Starting...");
		this.running = true;
	}

	/**
	 * Sets the game's running status to false.
	 */
	@Override
	public synchronized void stop() {
		if (!this.running)
			return;

		LOGGER.info("Stopping...");
		this.running = false;
		this.world.stop();
		this.server.close();
	}

	// TODO improve loop
	@Override
	public void run() {
		try {
			this.init();
			this.serverReady = true;
			this.server.bindInternally();
			
		} catch (Throwable t) {
			t.printStackTrace();
		}

		while (true) {
			try {
				while (this.running) {

					this.timer.updateTimer();

					for (int i = 0; i < Math.min(10, this.timer.elapsedTicks); ++i) {
						update();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				this.stop();
			}
			
			break;
		}
	}

	private void update()
	{
		if(StateManager.getActiveState() != null)
		{
			StateManager.getActiveState().update();
		}
	}

	@Override
	protected void init() {
		
		this.timer = new Timer(20);
		
		this.world = new World("world");
		World world = ZerraServer.getInstance().getWorld();
		Layer layer = world.getLayer(0);
		for (int x = 0; x < 3; x++) {
			for (int z = 0; z < 3; z++) {
				layer.loadPlate(new Vector3i(x - 1, 0, z - 1));
			}
		}
		this.eventHandler = new EventHandler();
	}

	public static ZerraServer getInstance()
	{
		return (ZerraServer) instance;
	}

	public void schedule(Runnable runnable) {
		Validate.notNull(runnable);
		this.pool.execute(runnable);
	}
	
	public boolean isReady()
	{
		return this.serverReady;
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
	
	public ServerManager getServerManager()
	{
		return this.server;
	}
}