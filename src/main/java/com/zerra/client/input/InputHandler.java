package com.zerra.client.input;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.glfw.GLFW;

import com.zerra.client.input.gamepad.Gamepad;
import com.zerra.client.view.Display;

public class InputHandler {

	private byte[] keys;
	private byte[] mouseButtons;
	private Map<Integer, Gamepad> gamepads;

	public InputHandler() {
		this.keys = new byte[GLFW.GLFW_KEY_LAST];
		this.mouseButtons = new byte[GLFW.GLFW_MOUSE_BUTTON_LAST];
		this.gamepads = new HashMap<Integer, Gamepad>();
		for(int jid = 0; jid < Display.getAllPresentJoysticks().length; jid++) {
			if(Display.isJoystickPresent(jid)) {
				this.onJoystickConnected(jid);
			}
		}
	}

	public boolean isKeyPressed(int keyCode) {
		return this.keys[keyCode] == 1;
	}

	public boolean isMouseButtonPressed(int mouseButton) {
		return this.mouseButtons[mouseButton] == 1;
	}

	public void setKeyPressed(int keyCode, boolean pressed) {
		this.keys[keyCode] = (byte) (pressed ? 1 : 0);
	}

	public void setMouseButtonPressed(int mouseButton, boolean pressed) {
		this.mouseButtons[mouseButton] = (byte) (pressed ? 1 : 0);
	}

	public void updateGamepad() {
		for (int jid : this.gamepads.keySet()) {
			Gamepad gamepad = this.gamepads.get(jid);
			if (GLFW.glfwJoystickPresent(jid)) {
				gamepad.update();
			} else {
				this.onJoystickDisconnected(jid);
			}
		}
	}

	public void onJoystickConnected(int jid) {
		if (this.gamepads.containsKey(jid)) {
			this.gamepads.get(jid).invalidate();
			this.gamepads.remove(jid);
		}
		this.gamepads.put(jid, new Gamepad(jid));
	}

	public void onJoystickDisconnected(int jid) {
		this.gamepads.remove(jid);
	}
	
	public boolean isGamepadConnected(int jid) {
		return this.gamepads.containsKey(jid);
	}

	public Gamepad getGamepad(int jid) {
		return this.gamepads.get(jid);
	}
}