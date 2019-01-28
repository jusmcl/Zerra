package com.zerra.client.gfx.texture;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.GL11;

import com.zerra.client.gfx.texture.map.TextureMap;
import com.zerra.client.util.Loader;
import com.zerra.client.util.LoadingUtils;
import com.zerra.client.util.ResourceLocation;
import com.zerra.common.world.tile.Tile;
import com.zerra.common.world.tile.Tiles;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * Used in managing textures.
 *
 * @author Ocelot5836
 */
public class TextureManager
{

	public static final ResourceLocation MISSING_TEXTURE_LOCATION = new ResourceLocation("missingno");

	@Nonnull
	private ResourceLocation boundTextureLocation;
	private Map<ResourceLocation, ITexture> textures;
	private TextureMap textureMap;

	public TextureManager()
	{
		textures = new HashMap<ResourceLocation, ITexture>();
		this.loadTexture(MISSING_TEXTURE_LOCATION, Loader.loadTexture(LoadingUtils.createMissingImage(256, 256)));
		boundTextureLocation = MISSING_TEXTURE_LOCATION;
		this.textureMap = new TextureMap(new ResourceLocation("atlas"), this);
	}

	/**
	 * Loads a texture up from a resource location.
	 * 
	 * @param location The location to load the texture from.
	 * @param texture The texture to load.
	 */
	public void loadTexture(ResourceLocation location, ITexture texture)
	{
		textures.put(location, texture);
	}

	/**
	 * @return The texture map the game uses.
	 */
	public TextureMap getTextureMap()
	{
		return textureMap;
	}

	/**
	 * Binds a texture to a resource location.
	 * 
	 * @param location The resource location to bind the texture to.
	 */
	public void bind(ResourceLocation location)
	{
		if (location == null)
			return;
		if (textures.get(location) == null || (textures.get(location).getId() == -1))
		{
			textures.put(location, Loader.loadTexture(location));
		}
		this.boundTextureLocation = location;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures.get(location).getId());
	}

	/**
	 * Deletes all textures that have been cached and uploaded into memory.
	 */
	public void dispose()
	{
		for (ResourceLocation location : this.textures.keySet())
		{
			ITexture texture = this.textures.get(location);
			if (texture != null)
			{
				texture.delete();
			}
		}
	}

	/**
	 * Gets a texture from a resource location.
	 * 
	 * @param location The resource location to get the texture from.
	 * @return The texture retrieved from the resource location.
	 */
	public ITexture getTexture(ResourceLocation location)
	{
		if (location == null)
			location = MISSING_TEXTURE_LOCATION;
		bind(location);
		return textures.get(location);
	}

	/**
	 * @return The currently bound texture location
	 */
	public ResourceLocation getBoundTextureLocation()
	{
		return boundTextureLocation;
	}
}