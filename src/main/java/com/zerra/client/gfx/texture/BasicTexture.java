package com.zerra.client.gfx.texture;

import org.lwjgl.opengl.GL11;

/**
 * 
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * A basic texture object.
 *
 * @author Ocelot5836
 */
public class BasicTexture implements ITexture {

	private int textureId;
	private int width;
	private int height;

	public BasicTexture(int textureId, int width, int height) {
		this.textureId = textureId;
		this.width = width;
		this.height = height;
	}

	@Override
	public void delete() {
		if (this.textureId != -1) {
			GL11.glDeleteTextures(this.textureId);
			this.textureId = -1;
		}
	}

	@Override
	public int getId() {
		return textureId;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}
}