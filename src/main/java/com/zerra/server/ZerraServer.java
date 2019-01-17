package com.zerra.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3i;

import com.zerra.Launch;
import com.zerra.client.state.StateManager;
import com.zerra.client.state.WorldState;
import com.zerra.client.util.Timer;
import com.zerra.common.event.EventHandler;
import com.zerra.common.world.World;
import com.zerra.common.world.storage.Layer;
import com.zerra.server.network.ServerPacketManager;

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
public class ZerraServer implements Runnable {

	private static final Logger LOGGER = LogManager.getLogger(Launch.NAME);

	private static ZerraServer instance;

	private ExecutorService pool;
	private boolean running;
	private boolean serverReady = false;

	private Timer timer;
	protected World world;
	
	private EventHandler eventHandler;
	
	private ServerPacketManager server;

	public ZerraServer() {
		instance = this;
		this.pool = Executors.newCachedThreadPool();
		
		server = new ServerPacketManager();
		
		this.start();
	}

	/**
	 * Sets the game's running status to true.
	 */
	public synchronized void start() {
		if (this.running)
			return;

		LOGGER.info("Starting...");
		this.running = true;
	}

	/**
	 * Sets the game's running status to false.
	 */
	public synchronized void stop() {
		if (!this.running)
			return;

		LOGGER.info("Stopping...");
		this.running = false;
		this.world.stop();
	}

	// TODO improve loop
	@Override
	public void run() {
		try {
			this.init();
			this.serverReady = true;
			this.server.bind();
			
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
		StateManager.getActiveState().update();
	}

	private void init() throws Throwable {
		
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
		
		//TODO: Eventually set the first state of the game to the game loading state.
		StateManager.setActiveState(new WorldState());
	}

	public void schedule(Runnable runnable) {
		Validate.notNull(runnable);
		this.pool.execute(runnable);
	}

	public World getWorld() {
		return world;
	}
	
	public EventHandler getEventHandler() {
		return eventHandler;
	}

	public static Logger logger() {
		return LOGGER;
	}

	public static ZerraServer getInstance() {
		return instance;
	}
	
	public boolean isReady()
	{
		return this.serverReady;
	}
}