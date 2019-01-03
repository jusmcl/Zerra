package com.zerra.client.view;

import org.joml.Vector3f;

/**
 * <em><b>Copyright (c) 2018 The Zerra Team.</b></em>
 * 
 * <br>
 * </br>
 * 
 * Allows easy movement of the objects in the world to simulate a camera.
 * 
 * @author Ocelot5836
 */
public interface ICamera {

	Vector3f getPosition();

	Vector3f getRotation();

	Vector3f getDirection();
}