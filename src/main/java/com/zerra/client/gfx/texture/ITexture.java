package com.zerra.client.gfx.texture;

public interface ITexture {

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