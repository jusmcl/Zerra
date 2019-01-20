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
import com.zerra.client.network.ClientConnectionManager;
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
import com.zerra.common.network.msg.MessageDisconnect;
import com.zerra.common.util.MiscUtils;
import com.zerra.common.world.tile.Tile;
import com.zerra.common.world.tile.Tiles;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * The main client game class.
 * 
 * @author Ocelot5836
 * @author Tebreca
 * @author Arpaesis
 */
public class ZerraClient extends Zerra
{

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

	private ClientConnectionManager client;

	public ZerraClient()
	{
		instance = this;
		this.pool = Executors.newCachedThreadPool();

		this.client = new ClientConnectionManager();

		this.start();
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
	 * Shuts down the internal server and stops the game loop.
	 */
	@Override
	public synchronized void stop()
	{
		if (!this.running)
			return;

		LOGGER.info("Stopping...");
		// TODO: Only attempt a disconnect if the server is even alive to begin with.
		this.client.getPacketSender().sendToServer(new MessageDisconnect(this.getConnectionManager().getUUID().toString()));
		this.running = false;
	}

	// TODO improve loop
	@Override
	public void run()
	{
		try
		{
			this.init();
		} catch (Throwable t)
		{
			t.printStackTrace();
		}

		while (true)
		{
			try
			{

				while (this.running)
				{
					checkRequestedExit();

					this.timer.updateTimer();

					for (int i = 0; i < Math.min(10, this.timer.elapsedTicks); ++i)
					{
						update();
					}

					GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
					this.render(this.timer.renderPartialTicks);
				}
			} catch (Exception e)
			{
				e.printStackTrace();
				this.stop();
			}
			this.cleanupResources();
			break;
		}
	}

	/**
	 * Renders the game.
	 * 
	 * @param partialTicks - The partial ticks used in rendering.
	 */
	private void render(float partialTicks)
	{
		if (StateManager.getActiveState() != null)
		{
			StateManager.getActiveState().render();
		}
	}

	/**
	 * Updates controls and the client world.
	 */
	private void update()
	{
		this.camera.update();
		this.inputHandler.updateGamepad();
	}

	/**
	 * Initializes the game, including setting up the display, renderers, and input
	 * handlers.
	 */
	@Override
	protected void init()
	{
		Display.createDisplay(ClientLaunch.NAME + " v" + ClientLaunch.VERSION, 1280, 720);
		Display.setIcon(new ResourceLocation("icons/16.png"), new ResourceLocation("icons/32.png"));
		// TODO:
		// StateManager.setActiveState(new GameLoadState(1280, 720, 500, 20, 2));
		// completeLoadingStep();
		I18n.setLanguage(new Locale("en", "us"));
		Tiles.registerTiles();
		this.timer = new Timer(20);
		this.textureManager = new TextureManager();
		this.textureMap = new TextureMap(new ResourceLocation("atlas"), this.textureManager);
		Tile[] tiles = Tiles.getTiles();
		for (Tile tile : tiles)
		{
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

	private void completeLoadingStep()
	{
		this.loadingProgress++;
		StateManager.getActiveState().update();
		StateManager.getActiveState().render();
	}

	/**
	 * Checks whether or not a request to close the display has been made.
	 */
	private void checkRequestedExit()
	{
		if (!Display.isCloseRequested())
			Display.update();
		else
			this.stop();
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
	 * Fires when a key is pressed.
	 * 
	 * @param keyCode - The key code given by the key pressed.
	 */
	public void onKeyPressed(int keyCode)
	{
		this.inputHandler.setKeyPressed(keyCode, true);
	}

	/**
	 * Fires when a key is released.
	 * 
	 * @param keyCode - The key code given by the key released.
	 */
	public void onKeyReleased(int keyCode)
	{
		this.inputHandler.setKeyPressed(keyCode, false);
	}

	/**
	 * Fires when a mouse button is pressed.
	 * 
	 * @param mouseX - The x position of the mouse on screen when a mouse button is
	 *        pressed.
	 * @param mouseY - The y position of the mouse on screen when a mouse button is
	 *        pressed.
	 * @param mouseButton - The mouse button pressed, given as an int.
	 */
	public void onMousePressed(double mouseX, double mouseY, int mouseButton)
	{
		this.inputHandler.setMouseButtonPressed(mouseButton, true);
	}

	/**
	 * Fires when a mouse button is released.
	 * 
	 * @param mouseX - The x position of the mouse on screen when a mouse button is
	 *        released.
	 * @param mouseY - The y position of the mouse on screen when a mouse button is
	 *        released.
	 * @param mouseButton - The mouse button released, given as an int.
	 */
	public void onMouseReleased(double mouseX, double mouseY, int mouseButton)
	{
		this.inputHandler.setMouseButtonPressed(mouseButton, false);
	}

	/**
	 * Fires when the mouse scrolls.
	 * 
	 * @param mouseX - The x position of the mouse as it scrolls.
	 * @param mouseY - The y position of the mouse as it scrolls.
	 * @param yoffset - How much the mouse scrolled.
	 */
	public void onMouseScrolled(double mouseX, double mouseY, double yoffset)
	{
	}

	/**
	 * Fires when a joystick button is pressed.
	 * 
	 * @param jid - The id of the joystick.
	 * @param button - The button pressed given as an int.
	 */
	public void onJoystickButtonPressed(int jid, int button)
	{
	}

	/**
	 * Fires when a joystick button is released.
	 * 
	 * @param jid - The id of the joystick.
	 * @param button - The button released given as an int.
	 */
	public void onJoystickButtonReleased(int jid, int button)
	{
	}

	/**
	 * Fires when a joystick is connected.
	 * 
	 * @param jid - The id of the joystick.
	 */
	public void onJoystickConnected(int jid)
	{
		this.inputHandler.onGamepadConnected(jid);
	}

	/**
	 * Fires when a joystick is disconnected.
	 * 
	 * @param jid - The id of the joystick.
	 */
	public void onJoystickDisconnected(int jid)
	{
		this.inputHandler.onGamepadDisconnected(jid);
	}

	/**
	 * Cleans up the resources when the game closes.
	 */
	public void cleanupResources()
	{
		long startTime = System.currentTimeMillis();
		Display.destroy();
		Loader.cleanUp();
		this.textureManager.dispose();
		this.pool.shutdown();
		instance = null;
		logger().info("Cleaned up all resources in " + MiscUtils.secondsSinceTime(startTime) + " seconds");
	}

	/**
	 * @return The loading progress of the game.
	 */
	public float getLoadingPercentage()
	{
		return loadingSteps / loadingProgress;
	}

	/**
	 * @return The render partial ticks of the game's rendering cycle.
	 */
	public float getRenderPartialTicks()
	{
		return timer.renderPartialTicks;
	}

	/**
	 * @return The texture manager for the game.
	 */
	public TextureManager getTextureManager()
	{
		return textureManager;
	}

	/**
	 * @return The texture map the game uses.
	 */
	public TextureMap getTextureMap()
	{
		return textureMap;
	}

	/**
	 * @return The input handler for the client.
	 */
	public InputHandler getInputHandler()
	{
		return inputHandler;
	}

	/**
	 * @return The event handler for the client.
	 */
	public EventHandler getEventHandler()
	{
		return eventHandler;
	}

	/**
	 * @return The logger for the client.
	 */
	public static Logger logger()
	{
		return LOGGER;
	}

	/**
	 * @return The client instance.
	 */
	public static ZerraClient getInstance()
	{
		return instance;
	}

	/**
	 * @return Whether the game is running or not.
	 */
	public boolean isRunning()
	{
		return running;
	}

	/**
	 * @return The number of ticks there are in a second for the game.
	 */
	public float getTicksPerSecond()
	{
		return timer.getTicksPerSecond();
	}

	/**
	 * @return The camera for the client.
	 */
	public Camera getCamera()
	{
		return camera;
	}

	/**
	 * @return The FBO for the client.
	 */
	public Fbo getFbo()
	{
		return fbo;
	}

	/**
	 * @return The GUI renderer for the client.
	 */
	public GuiRenderer getGuiRenderer()
	{
		return guiRenderer;
	}

	/**
	 * @return The tile renderer for the client.
	 */
	public TileRenderer getTileRenderer()
	{
		return tileRenderer;
	}

	/**
	 * @return The mod manager for the client.
	 */
	public ModManager getModManager()
	{
		return modManager;
	}

	/**
	 * @return The connection manager for the client.
	 */
	public ClientConnectionManager getConnectionManager()
	{
		return this.client;
	}
}