package com.zerra.client.renderer.texture;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.GL11;

import com.zerra.client.util.Loader;
import com.zerra.client.util.LoadingUtils;
import com.zerra.client.util.ResourceLocation;

public class TextureManager {

	public static final ResourceLocation MISSING_TEXTURE_LOCATION = new ResourceLocation("missingno");

	@Nonnull
	private ResourceLocation boundTextureLocation;
	private Map<String, ITexture> textures;

	public TextureManager() {
		textures = new HashMap<String, ITexture>();
		this.load(MISSING_TEXTURE_LOCATION, Loader.loadTexture(LoadingUtils.createMissingImage(256, 256)));
		boundTextureLocation = MISSING_TEXTURE_LOCATION;
	}

	/**
	 * Loads a texture up from a resource location.
	 * 
	 * @param location
	 *            The location to load the texture from.
	 * 
	 * @param texture
	 *            The texture to load.
	 */
	public void loadTexture(ResourceLocation location, ITexture texture) {
		textures.put(location.toString(), texture);
	}

	/**
	 * Binds a texture to a resource location.
	 * 
	 * @param location
	 *            The resource location to bind the texture to.
	 */
	public void bind(ResourceLocation location) {
		if (location == null)
			return;
		if (textures.get(location.toString()) == null) {
			textures.put(location.toString(), Loader.loadTexture(location));
		}
		this.boundTextureLocation = location;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures.get(location.toString()).getId());
	}

	/**
	 * Gets a texture from a resource location.
	 * 
	 * @param location
	 *            The resource location to get the texture from.
	 * 
	 * @return The texture retrieved from the resource location.
	 */
	public ITexture getTexture(ResourceLocation location) {
		if (location == null)
			location = MISSING_TEXTURE_LOCATION;
		bind(location);
		return textures.get(location.toString());
	}

	/**
	 * @return The currently bound texture location
	 */
	public ResourceLocation getBoundTextureLocation() {
		return boundTextureLocation;
	}

	/**
	 * Loads a texture into the texture manager from a resource location.
	 * 
	 * @param location
	 *            The resource location to load the texture from.
	 * 
	 * @param texture
	 *            The texture to load.
	 */
	public void load(ResourceLocation location, ITexture texture) {
		textures.put(location.toString(), texture);
	}
}