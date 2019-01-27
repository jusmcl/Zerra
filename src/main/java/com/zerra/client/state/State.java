package com.zerra.client.state;

import com.zerra.client.ZerraClient;
import com.zerra.server.ZerraServer;

public abstract class State
{

	private String name;

	protected ZerraClient zerraClient;
	protected ZerraServer zerraServer;

	public State(String name)
	{
		this.name = name;
		this.zerraClient = ZerraClient.getInstance();
		reloadServerInstance();
		ZerraClient.logger().info("Loading " + this.getName() + " state.");
	}

	public void reloadServerInstance()
	{
		this.zerraServer = ZerraServer.getInstance();
	}

	// No rendering can ever be done in this method
	public abstract void update();

	public abstract void render();

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Fires when a key is pressed.
	 * 
	 * @param keyCode - The key code given by the key pressed.
	 */
	public void onKeyPressed(int keyCode)
	{
		ZerraClient.getInstance().getInputHandler().setKeyPressed(keyCode, true);
	}

	/**
	 * Fires when a key is released.
	 * 
	 * @param keyCode - The key code given by the key released.
	 */
	public void onKeyReleased(int keyCode)
	{
		ZerraClient.getInstance().getInputHandler().setKeyPressed(keyCode, false);
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
		ZerraClient.getInstance().getInputHandler().setMouseButtonPressed(mouseButton, true);
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
		ZerraClient.getInstance().getInputHandler().setMouseButtonPressed(mouseButton, false);
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
		ZerraClient.getInstance().getInputHandler().onGamepadConnected(jid);
	}

	/**
	 * Fires when a joystick is disconnected.
	 * 
	 * @param jid - The id of the joystick.
	 */
	public void onJoystickDisconnected(int jid)
	{
		ZerraClient.getInstance().getInputHandler().onGamepadDisconnected(jid);
	}
}
