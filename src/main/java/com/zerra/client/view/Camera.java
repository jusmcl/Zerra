package com.zerra.client.view;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Camera implements ICamera {

	private Vector3f position;
	private Vector3f rotation;
	private Vector3f direction;

	public Camera() {
		this.position = new Vector3f();
		this.rotation = new Vector3f();
		this.direction = new Vector3f();
	}

	public void update() {
		this.position.add(this.direction);
	}

	public void onKeyPressed(int keyCode) {
		if (keyCode == GLFW.GLFW_KEY_W) {
			this.direction.y = -1;
		}
		if (keyCode == GLFW.GLFW_KEY_S) {
			this.direction.y = 1;
		}
		if (keyCode == GLFW.GLFW_KEY_A) {
			this.direction.x = -1;
		}
		if (keyCode == GLFW.GLFW_KEY_D) {
			this.direction.x = 1;
		}
	}

	public void onKeyReleased(int keyCode) {
		if (keyCode == GLFW.GLFW_KEY_W) {
			this.direction.y = 0;
		}
		if (keyCode == GLFW.GLFW_KEY_S) {
			this.direction.y = 0;
		}
		if (keyCode == GLFW.GLFW_KEY_A) {
			this.direction.x = 0;
		}
		if (keyCode == GLFW.GLFW_KEY_D) {
			this.direction.x = 0;
		}
	}

	@Override
	public Vector3f getPosition() {
		return position;
	}

	@Override
	public Vector3f getRotation() {
		return rotation;
	}

	@Override
	public Vector3f getDirection() {
		return direction;
	}
}