package com.zerra.client.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.glfw.GLFW;

import com.zerra.client.ZerraClient;
import com.zerra.client.gfx.Display;
import com.zerra.client.input.gamepad.Gamepad;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * Handles all input passed in from the {@link Display}'s handlers into {@link ZerraClient}.
 * 
 * @author Ocelot5836
 */
public class InputHandler
{
	private List<Integer> activeKeys;
	private List<Integer> activeMouseButtons;
	private Map<Integer, Gamepad> gamepads;

	public InputHandler()
	{
		this.activeKeys = new ArrayList<Integer>();
		this.activeMouseButtons = new ArrayList<Integer>();
		this.gamepads = new HashMap<Integer, Gamepad>();
		for (int jid = 0; jid < Display.getAllPresentJoysticks().length; jid++)
		{
			if (Display.isJoystickPresent(jid))
			{
				this.onGamepadConnected(jid);
			}
		}
	}

	/**
	 * @return The number of keys pressed on the keyboard
	 */
	public int getNumberOfKeysPressed()
	{
		return this.activeKeys.size();
	}

	/**
	 * @return The number of buttons pressed on the mouse
	 */
	public int getNumberOfMouseButtonsPressed()
	{
		return this.activeMouseButtons.size();
	}

	/**
	 * @return Whether or not ANY mouse button is pressed
	 */
	public boolean isReceivingMouseInput()
	{
		return this.activeMouseButtons.size() > 0;
	}

	/**
	 * Checks to see if the key code supplied has been pressed.
	 * 
	 * @param keyCode
	 *            The code of the key
	 * @return Whether or not that key was pressed
	 */
	public boolean isKeyPressed(int keyCode)
	{
		return this.activeKeys.contains(keyCode);
	}

	/**
	 * Checks to see if the mouse button supplied is pressed.
	 * 
	 * @param mouseButton
	 *            The button to check
	 * @return Whether or not that button is pressed
	 */
	public boolean isMouseButtonPressed(int mouseButton)
	{
		return this.activeMouseButtons.contains(mouseButton);
	}

	/**
	 * Sets the key with the supplied key code to be pressed or not.
	 * 
	 * @param keyCode
	 *            The key code to set pressed
	 * @param pressed
	 *            Whether or not the key should be pressed
	 */
	public void setKeyPressed(int keyCode, boolean pressed)
	{
		if (pressed)
		{
			this.activeKeys.add(keyCode);
		}
		else
		{
			this.activeKeys.remove((Integer)keyCode);
		}
	}

	/**
	 * Sets the supplied mouse button to be pressed or not.
	 * 
	 * @param mouseButton
	 *            The button to set to be pressed
	 * @param pressed
	 *            Whether or not the button should be pressed
	 */
	public void setMouseButtonPressed(int mouseButton, boolean pressed)
	{
		if (pressed)
		{
			this.activeMouseButtons.add(mouseButton);
		}
		else
		{
			this.activeMouseButtons.remove((Integer)mouseButton);
		}
	}

	/**
	 * Updates all of the gamepads connected to the device.
	 */
	public void updateGamepad()
	{
		for (int jid : this.gamepads.keySet())
		{
			Gamepad gamepad = this.gamepads.get(jid);
			if (GLFW.glfwJoystickPresent(jid))
			{
				gamepad.update();
			}
			else
			{
				this.onGamepadDisconnected(jid);
			}
		}
	}

	/**
	 * Registers a gamepad as being connected.
	 * 
	 * @param jid
	 *            The id of the joystick
	 */
	public void onGamepadConnected(int jid)
	{
		if (this.gamepads.containsKey(jid))
		{
			this.gamepads.get(jid).invalidate();
			this.gamepads.remove(jid);
		}
		this.gamepads.put(jid, new Gamepad(jid));
	}

	/**
	 * Registers a joystick as being disconnected.
	 * 
	 * @param jid
	 *            The id of the joystick
	 */
	public void onGamepadDisconnected(int jid)
	{
		this.gamepads.remove(jid);
	}

	/**
	 * Checks to see if a gamepad is connected.
	 * 
	 * @param jid
	 *            The id of the joystick
	 * @return Whether or not the gamepad is connected
	 */
	public boolean isGamepadConnected(int jid)
	{
		return this.gamepads.containsKey(jid);
	}

	/**
	 * Checks to see if a gamepad is connected to the device.
	 * 
	 * @param jid
	 *            The id of the joystick
	 * @return The gamepad or null if it is not connected
	 */
	public Gamepad getGamepad(int jid)
	{
		return this.gamepads.get(jid);
	}
}