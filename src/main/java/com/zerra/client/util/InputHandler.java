package com.zerra.client.util;

import org.lwjgl.glfw.GLFW;

public class InputHandler {

	private byte[] keys;
	private byte[] mouseButtons;

	public InputHandler() {
		this.keys = new byte[GLFW.GLFW_KEY_LAST];
		this.mouseButtons = new byte[GLFW.GLFW_MOUSE_BUTTON_LAST];
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
	}
}