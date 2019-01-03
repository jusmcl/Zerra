package com.zerra.client.view;

import org.joml.Vector3f;

public class Camera implements ICamera {

	private Vector3f position;
	private Vector3f rotation;
	private Vector3f direction;

	public Camera() {
		this.position = new Vector3f();
		this.rotation = new Vector3f();
		this.direction = new Vector3f();
	}

	public void onKeyPressed(int keyCode) {

	}

	public void onKeyReleased(int keyCode) {

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