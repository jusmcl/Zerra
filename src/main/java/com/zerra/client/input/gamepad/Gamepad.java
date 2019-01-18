package com.zerra.client.input.gamepad;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.glfw.GLFW;

import com.zerra.client.ZerraClient;

public class Gamepad {

	private int jid;
	private Joystick[] joysticks;
	private byte[] previousButtons;
	private byte[] buttons;

	public Gamepad(int jid) {
		this.jid = jid;

		FloatBuffer joystickBuffer = GLFW.glfwGetJoystickAxes(jid);
		this.joysticks = new Joystick[joystickBuffer.limit() / 2];
		for (int i = 0; i < this.joysticks.length; i++) {
			this.joysticks[i] = new Joystick();
		}

		ByteBuffer buttonBuffer = GLFW.glfwGetJoystickButtons(jid);
		this.previousButtons = new byte[buttonBuffer.limit()];
		this.buttons = new byte[buttonBuffer.limit()];
		for (int i = 0; i < this.buttons.length; i++) {
			this.previousButtons[i] = buttonBuffer.get(i);
			this.buttons[i] = buttonBuffer.get(i);
		}
	}

	public void update() {
		if (this.isValid()) {
			FloatBuffer axes = GLFW.glfwGetJoystickAxes(this.jid);
			for (int i = 0; i < axes.limit() / 2; i++) {
				this.joysticks[i].updateAxes(axes.get(i * 2), axes.get(i * 2 + 1));
			}

			ByteBuffer buttons = GLFW.glfwGetJoystickButtons(jid);
			for (int i = 0; i < this.buttons.length; i++) {
				this.previousButtons[i] = this.buttons[i];
				this.buttons[i] = buttons.get(i);
				if (this.previousButtons[i] != this.buttons[i]) {
					if (this.isButtonPressed(i)) {
						ZerraClient.getInstance().onJoystickButtonPressed(this.jid, i);
					} else {
						ZerraClient.getInstance().onJoystickButtonReleased(this.jid, i);
					}
				}
			}
		}
	}

	public void invalidate() {
		this.jid = -1;
	}

	public boolean isValid() {
		return this.jid != -1;
	}

	public boolean isJoystickPresent(int joystick) {
		return joystick >= 0 && joystick < this.joysticks.length;
	}

	public boolean isButtonPressed(int button) {
		return (button >= 0 && button < this.buttons.length ? this.buttons[button] : 0) == 1;
	}

	public Joystick getJoystick(int joystick) {
		if (!this.isJoystickPresent(joystick))
			return null;
		return joysticks[joystick];
	}
}