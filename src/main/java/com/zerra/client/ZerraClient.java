package com.zerra.client;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import com.zerra.ClientLaunch;
import com.zerra.api.mod.ModManager;
import com.zerra.client.gfx.renderer.GuiRenderer;
import com.zerra.client.gfx.renderer.tile.TileRenderer;
import com.zerra.client.gfx.texture.TextureManager;
import com.zerra.client.gfx.texture.map.TextureMap;
import com.zerra.client.input.InputHandler;
import com.zerra.client.network.ClientManager;
import com.zerra.client.state.StateManager;
import com.zerra.client.util.Fbo;
import com.zerra.client.util.I18n;
import com.zerra.client.util.Loader;
import com.zerra.client.util.ResourceLocation;
import com.zerra.client.util.Timer;
import com.zerra.client.view.Camera;
import com.zerra.client.view.Display;
import com.zerra.common.Zerra;
import com.zerra.common.event.EventHandler;
import com.zerra.common.network.msg.MessageShutdownInternalServer;
import com.zerra.common.util.MiscUtils;
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
public class ZerraClient extends Zerra {

	private static final Logger LOGGER = LogManager.getLogger(ClientLaunch.NAME);

	private static ZerraClient instance;

	private ExecutorService pool;
	private boolean running;
	
	private int loadingProgress;
	private int loadingSteps;

	private Timer timer;
	private TextureManager textureManager;
	private TextureMap textureMap;
	protected TileRenderer tileRenderer;
	protected GuiRenderer guiRenderer;
	protected Camera camera;
	protected InputHandler inputHandler;
	protected Fbo fbo;
	
	private EventHandler eventHandler;
	
	private ModManager modManager;
	
	private ClientManager client;

	public ZerraClient() {
		instance = this;
		this.pool = Executors.newCachedThreadPool();

		this.client = new ClientManager();
		
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
	 * Shuts down the internal server and stops the game loop.
	 */
	@Override
	public synchronized void stop() {
		if (!this.running)
			return;

		LOGGER.info("Stopping...");
		this.client.getPacketSender().sendToServer(new MessageShutdownInternalServer());
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
					checkRequestedExit();

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
			this.cleanupResources();
			break;
		}
	}

	private void render(float partialTicks)
	{
		if (StateManager.getActiveState() != null)
		{
			StateManager.getActiveState().render();
		}
	}
	
	private void update()
	{
		this.camera.update();
		this.inputHandler.updateGamepad();
	}

	@Override
	protected void init() {
		Display.createDisplay(ClientLaunch.NAME + " v" + ClientLaunch.VERSION, 1280, 720);
		Display.setIcon(new ResourceLocation("icons/16.png"), new ResourceLocation("icons/32.png"));
		//TODO:
		//StateManager.setActiveState(new GameLoadState(1280, 720, 500, 20, 2));
		//completeLoadingStep();
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
		this.tileRenderer = new TileRenderer();
		this.guiRenderer = new GuiRenderer();
		this.camera = new Camera();
		this.inputHandler = new InputHandler();
		this.fbo = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_RENDER_BUFFER, 2);
		
		modManager = new ModManager();
		modManager.setupMods();
	}
	
	private void completeLoadingStep() {
		this.loadingProgress++;
		StateManager.getActiveState().update();
		StateManager.getActiveState().render();
	}
	
	
	private void checkRequestedExit() {
		if (!Display.isCloseRequested())
			Display.update();
		else
			this.stop();
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

	/**
	 * Cleans up the resources when the game closes.
	 */
	public void cleanupResources() {
		long startTime = System.currentTimeMillis();
		Display.destroy();
		Loader.cleanUp();
		this.textureManager.dispose();
		this.pool.shutdown();
		instance = null;
		logger().info("Cleaned up all resources in " + MiscUtils.secondsSinceTime(startTime) + " seconds");
	}
	
	public float getLoadingPercentage() {
		return loadingSteps / loadingProgress;
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
	
	public EventHandler getEventHandler() {
		return eventHandler;
	}

	public static Logger logger() {
		return LOGGER;
	}

	public static ZerraClient getInstance() {
		return instance;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public float getTicksPerSecond()
	{
		return timer.getTicksPerSecond();
	}
	
	public float renderPartialTicks()
	{
		return this.timer.renderPartialTicks;
	}
	
	public Camera getCamera() 
	{
		return camera;
	}

	public Fbo getFbo() 
	{
		return fbo;
	}
	
	public GuiRenderer getGuiRenderer() {
		return guiRenderer;
	}

	public TileRenderer getTileRenderer() {
		return tileRenderer;
	}

    public ModManager getModManager() {
        return modManager;
    }
    
    public ClientManager getClientManager()
    {
    	return this.client;
    }
}