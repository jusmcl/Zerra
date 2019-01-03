package com.zerra.client.view;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.zerra.client.Zerra;

public class Camera implements ICamera {

	private Vector3f renderPosition;
	private Vector3f lastPosition;
	private Vector3f position;
	private Vector3f renderRotation;
	private Vector3f lastRotation;
	private Vector3f rotation;
	private Vector3f direction;

	public Camera() {
		this.renderPosition = new Vector3f();
		this.lastPosition = new Vector3f();
		this.position = new Vector3f();
		this.renderRotation = new Vector3f();
		this.lastRotation = new Vector3f();
		this.rotation = new Vector3f();
		this.direction = new Vector3f();
	}

	public void update() {
		this.lastPosition.set(this.position);
		this.lastRotation.set(this.rotation);
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
		return this.renderPosition.set(this.lastPosition.x + (this.position.x - this.lastPosition.x) * Zerra.getInstance().getRenderPartialTicks(), this.lastPosition.y + (this.position.y - this.lastPosition.y) * Zerra.getInstance().getRenderPartialTicks(), this.lastPosition.z + (this.position.z - this.lastPosition.z) * Zerra.getInstance().getRenderPartialTicks());
	}

	@Override
	public Vector3f getRotation() {
		return this.renderRotation.set(this.renderRotation.x + (this.rotation.x - this.renderRotation.x) * Zerra.getInstance().getRenderPartialTicks(), this.renderRotation.y + (this.rotation.y - this.renderRotation.y) * Zerra.getInstance().getRenderPartialTicks(), this.renderRotation.z + (this.rotation.z - this.renderRotation.z) * Zerra.getInstance().getRenderPartialTicks());
	}
}