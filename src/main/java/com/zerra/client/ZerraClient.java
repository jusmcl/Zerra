package com.zerra.client;

import java.util.Locale;

import org.lwjgl.opengl.GL11;

import com.zerra.api.mod.ModManager;
import com.zerra.client.gfx.renderer.Renderer;
import com.zerra.client.input.InputHandler;
import com.zerra.client.network.ClientConnectionManager;
import com.zerra.client.state.GameLoadState;
import com.zerra.client.state.MenuState;
import com.zerra.client.state.StateManager;
import com.zerra.client.state.WorldState;
import com.zerra.client.util.I18n;
import com.zerra.client.util.Loader;
import com.zerra.client.view.Display;
import com.zerra.client.world.ClientWorld;
import com.zerra.common.Zerra;
import com.zerra.common.ZerraContentInit;
import com.zerra.common.event.EventHandler;
import com.zerra.common.util.MiscUtils;
import com.zerra.common.util.Timer;
import com.zerra.common.world.World;
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
	private static ZerraClient instance;

	private boolean running;

	private Timer timer;
	protected InputHandler inputHandler;

	private EventHandler eventHandler;
	private ModManager modManager;
	private RenderingManager renderingManager;

	private ClientConnectionManager clientConnection;
	private ClientWorld world;

	public ZerraClient()
	{
		super();
		instance = this;

		this.renderingManager = new RenderingManager();
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

		// Alert the server that we are disconnecting from it.
		if (StateManager.getActiveState() instanceof WorldState)
		{
			WorldState.cleanupWorldState();
		}

		this.running = false;

		super.stop();
	}

	// TODO improve loop
	@Override
	public void run()
	{

		this.init();

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
		this.cleanupResources();
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
			StateManager.getActiveState().render(Display.getMouseX() / Renderer.SCALE, Display.getMouseY() / Renderer.SCALE, this.getRenderPartialTicks());
		}
	}

	/**
	 * Updates controls and the client world.
	 */
	private void update()
	{
		this.renderingManager.getCamera().update();
		this.inputHandler.updateGamepad();
		if (StateManager.getActiveState() != null)
		{
			StateManager.getActiveState().update();
		}
	}

	/**
	 * Initializes the game, including setting up the display, renderers, and input
	 * handlers.
	 */
	@Override
	protected void init()
	{
		super.init();

		ZerraContentInit.init();
		// TODO: Move tiles registration into ZerraContentInit
		Tiles.registerTiles();

		this.timer = new Timer(20);
		this.renderingManager.init();
		this.inputHandler = new InputHandler();
		// TODO: Create the world and set the world in the ClientConnectionManager
		// instance

		this.modManager = new ModManager();
		this.modManager.setupMods();
		I18n.setLanguage(new Locale("en", "us"));

		this.clientConnection = new ClientConnectionManager(this);
		StateManager.setActiveState(new GameLoadState());
	}

	@Override
	public boolean isClient()
	{
		return true;
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
	 * Fires when a key is pressed.
	 * 
	 * @param keyCode - The key code given by the key pressed.
	 */
	public void onKeyPressed(int keyCode)
	{
		this.inputHandler.setKeyPressed(keyCode, true);
		if (StateManager.getActiveState() != null)
		{
			StateManager.getActiveState().onKeyPressed(keyCode);
		}
	}

	/**
	 * Fires when a key is released.
	 * 
	 * @param keyCode - The key code given by the key released.
	 */
	public void onKeyReleased(int keyCode)
	{
		this.inputHandler.setKeyPressed(keyCode, false);
		if (StateManager.getActiveState() != null)
		{
			StateManager.getActiveState().onKeyReleased(keyCode);
		}
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
		if (StateManager.getActiveState() != null)
		{
			StateManager.getActiveState().onMousePressed(mouseX, mouseY, mouseButton);
		}
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
		if (StateManager.getActiveState() != null)
		{
			StateManager.getActiveState().onMouseReleased(mouseX, mouseY, mouseButton);
		}
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
		this.renderingManager.getTextureManager().dispose();
		this.pool.shutdown();
		instance = null;
		logger().info("Cleaned up all resources in " + MiscUtils.secondsSinceTime(startTime));
	}

	/**
	 * @return The render partial ticks of the game's rendering cycle.
	 */
	public float getRenderPartialTicks()
	{
		return timer.renderPartialTicks;
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
	 * @return The rendering manager for the client.
	 */
	public RenderingManager getRenderingManager()
	{
		return renderingManager;
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
	@Override
	public ClientConnectionManager getConnectionManager()
	{
		return this.clientConnection;
	}

	public World createWorld(String name, long seed)
	{
		return this.world = new ClientWorld(name, seed);
	}

	@Override
	public ClientWorld getWorld()
	{
		return this.world;
	}
}