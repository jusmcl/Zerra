package com.zerra.client;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3i;
import org.lwjgl.opengl.GL11;

import com.zerra.Launch;
import com.zerra.client.gfx.renderer.tile.TileRenderer;
import com.zerra.client.gfx.texture.TextureManager;
import com.zerra.client.gfx.texture.map.TextureMap;
import com.zerra.client.input.InputHandler;
import com.zerra.client.util.I18n;
import com.zerra.client.util.Loader;
import com.zerra.client.util.ResourceLocation;
import com.zerra.client.util.Timer;
import com.zerra.client.view.Camera;
import com.zerra.client.view.Display;
import com.zerra.common.world.World;
import com.zerra.common.world.tile.Tile;
import com.zerra.common.world.tile.Tiles;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em>
 * 
 * <br>
 * </br>
 * 
 * The main client game class.
 * 
 * @author Ocelot5836, tebreca
 */
public class Zerra implements Runnable {

	private static final Logger LOGGER = LogManager.getLogger(Launch.NAME);

	private static Zerra instance;

	private Timer timer;
	private ExecutorService pool;
	private TextureManager textureManager;
	private TextureMap textureMap;
	private TileRenderer tileRenderer;
	private Camera camera;
	private InputHandler inputHandler;
	private World world;

	private boolean running;

	public Zerra() {
		instance = this;
		this.pool = Executors.newCachedThreadPool();
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
	}

	// TODO improve loop
	@Override
	public void run() {
		try {
			this.init();
		} catch (Throwable t) {
			t.printStackTrace();
		}

		while (true) {
			try {
				while (this.running) {
					if (!Display.isCloseRequested())
						Display.update();
					else
						running = false;

					this.timer.updateTimer();

					for (int i = 0; i < Math.min(10, this.timer.elapsedTicks); ++i) {
						update();
					}

					GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
					this.render(this.timer.renderPartialTicks);
				}
			} catch (Exception e) {
				e.printStackTrace();
				this.stop();
			}
			this.dispose();
			break;
		}
	}

	private void update() {
		this.camera.update();
		this.inputHandler.updateGamepad();
	}

	private void render(float partialTicks) {
		this.tileRenderer.renderTiles(this.camera, this.world, 0);
	}

	private void init() throws Throwable {
		Display.createDisplay(Launch.NAME + " v" + Launch.VERSION, 1280, 720);
		Display.setIcon(new ResourceLocation("icons/16.png"), new ResourceLocation("icons/32.png"));
		GL11.glClearColor(0, 0, 0, 1);

		I18n.setLanguage(new Locale("en", "us"));
		Tiles.registerTiles();
		this.timer = new Timer(20);
		this.textureManager = new TextureManager();
		this.textureMap = new TextureMap(new ResourceLocation("atlas"), this.textureManager);
		Tile[] tiles = Tiles.getTiles();
		for (Tile tile : tiles) {
			this.textureMap.register(tile.getTexture());
		}
		this.textureMap.stitch();
		this.world = new World();
		this.tileRenderer = new TileRenderer();
		this.camera = new Camera();
		this.inputHandler = new InputHandler();
		this.world.getLayer(0).getPlate(new Vector3i(0, 0, 0));
	}

	public void schedule(Runnable runnable) {
		Validate.notNull(runnable);
		this.pool.execute(runnable);
	}

	public void onKeyPressed(int keyCode) {
		this.inputHandler.setKeyPressed(keyCode, true);
	}

	public void onKeyReleased(int keyCode) {
		this.inputHandler.setKeyPressed(keyCode, false);
	}

	public void onMousePressed(double mouseX, double mouseY, int mouseButton) {
		this.inputHandler.setMouseButtonPressed(mouseButton, true);
	}

	public void onMouseReleased(double mouseX, double mouseY, int mouseButton) {
		this.inputHandler.setMouseButtonPressed(mouseButton, false);
	}

	public void onMouseScrolled(double mouseX, double mouseY, double yoffset) {
	}

	public void onJoystickButtonPressed(int jid, int button) {
	}

	public void onJoystickButtonReleased(int jid, int button) {
	}

	public void onJoystickConnected(int jid) {
		this.inputHandler.onGamepadConnected(jid);
	}

	public void onJoystickDisconnected(int jid) {
		this.inputHandler.onGamepadDisconnected(jid);
	}

	public void dispose() {
		long startTime = System.currentTimeMillis();
		Display.destroy();
		Loader.cleanUp();
		this.textureManager.dispose();
		this.pool.shutdown();
		instance = null;
		logger().info("Disposed of all resources in " + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds");
	}

	public float getRenderPartialTicks() {
		return timer.renderPartialTicks;
	}

	public TextureManager getTextureManager() {
		return textureManager;
	}

	public TextureMap getTextureMap() {
		return textureMap;
	}

	public InputHandler getInputHandler() {
		return inputHandler;
	}

	public static Logger logger() {
		return LOGGER;
	}

	public static Zerra getInstance() {
		return instance;
	}
}