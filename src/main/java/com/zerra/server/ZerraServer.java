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

import simplenet.Server;

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
	
	private Server server;

	public ZerraServer() {
		instance = this;
		this.pool = Executors.newCachedThreadPool();
		
		this.createServer();
		
		this.start();
	}
	
	public void createServer()
	{
		server = new Server();

		// Register one connection listener.
		server.onConnect(client -> {
			System.out.println(client + " has connected!");

		    client.readByteAlways(opcode -> {
		        switch (opcode) {
		            case 1:
		                client.readInt(System.out::println);
		        }
		    });

		    // Register an optional pre-disconnection listener.
		    client.preDisconnect(() -> System.out.println(client + " is about to disconnect!"));

		    // Register an optional post-disconnection listener.
		    client.postDisconnect(() -> System.out.println(client + " has successfully disconnected!"));
		});

		// Bind the server to an address and port AFTER registering listeners.
		server.bind("localhost", 43594);
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
		this.server.close();
	}

	// TODO improve loop
	@Override
	public void run() {
		try {
			this.init();
			this.serverReady = true;
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
		System.out.println("updating...");
		//StateManager.getActiveState().update();
	}

	private void init() throws Throwable {
		
		this.timer = new Timer(20);
		
		this.world = new World("world");
		World world = ZerraServer.getServer().getWorld();
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

	public static ZerraServer getServer() {
		return instance;
	}
	
	public boolean isServerReady()
	{
		return this.serverReady;
	}
}