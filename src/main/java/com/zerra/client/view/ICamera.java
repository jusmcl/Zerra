package com.zerra.client.view;

import org.joml.Vector3f;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em>
 * 
 * <br>
 * </br>
 * 
 * Allows easy movement of the objects in the world to simulate a camera.
 * 
 * @author Ocelot5836
 */
public interface ICamera {

	/**
	 * @return The three coordinates of the camera's position
	 */
	Vector3f getPosition();

	/**
	 * @return The three axes of the camera's rotation
	 */
	Vector3f getRotation();

}