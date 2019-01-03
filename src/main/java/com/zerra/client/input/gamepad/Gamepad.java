package com.zerra.client.input.gamepad;

import java.nio.FloatBuffer;

import javax.annotation.Nullable;

import org.lwjgl.glfw.GLFW;

public class Gamepad {

	private int jid;
	private Joystick[] joysticks;

	public Gamepad(int jid) {
		this.jid = jid;
		this.joysticks = new Joystick[GLFW.glfwGetJoystickAxes(jid).limit() / 2];
		for (int i = 0; i < this.joysticks.length; i++) {
			this.joysticks[i] = new Joystick();
		}
	}

	public void update() {
		if (this.isValid()) {
			FloatBuffer axes = GLFW.glfwGetJoystickAxes(this.jid);
			for (int i = 0; i < axes.limit() / 2; i++) {
				this.joysticks[i].updateAxes(axes.get(i * 2), axes.get(i * 2 + 1));
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

	@Nullable
	public Joystick getJoystick(int joystick) {
		if (!this.isJoystickPresent(joystick))
			return null;
		return joysticks[joystick];
	}
}