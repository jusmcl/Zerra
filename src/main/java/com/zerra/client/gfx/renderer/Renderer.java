package com.zerra.client.gfx.renderer;

import org.joml.Matrix4f;

import com.zerra.client.gfx.model.Model;
import com.zerra.client.view.Display;

/**
 * <em><b>Copyright (c) 2018 The Zerra Team.</b></em> <br>
 * </br>
 * Renders standard {@link Model}s based on how they were loaded into memory.
 * 
 * @author Ocelot5836
 */
public class Renderer
{

	// TODO put this in the game options
	public static final float SCALE = 3;
	public static final int MAX_LIGHTS = 6;

	@Deprecated
	private static final Matrix4f PROJECTION_MATRIX = new Matrix4f().ortho(0, Display.getWidth() / SCALE, Display.getHeight() / SCALE, 0, 0.3f, 1000.0f);

	@Deprecated
	public static Matrix4f getProjectionMatrix()
	{
		return PROJECTION_MATRIX;
	}
}