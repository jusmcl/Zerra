package com.zerra.client.gfx.texture;

/**
 * 
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * A basic interface for textures.
 *
 * @author Ocelot5836
 */
public interface ITexture
{

	/**
	 * Deletes this texture from memory.
	 */
	void delete();

	/**
	 * @return The ID of the texture.
	 */
	int getId();

	/**
	 * @return The width of the texture.
	 */
	int getWidth();

	/**
	 * @return The height of the texture.
	 */
	int getHeight();

}