package com.zerra.client;

import java.util.Locale;

import com.zerra.client.gfx.renderer.GuiRenderer;
import com.zerra.client.gfx.renderer.tile.TileRenderer;
import com.zerra.client.gfx.texture.TextureManager;
import com.zerra.client.gfx.texture.map.TextureMap;
import com.zerra.client.util.Fbo;
import com.zerra.client.util.I18n;
import com.zerra.client.util.ResourceLocation;
import com.zerra.client.view.Camera;
import com.zerra.client.view.Display;
import com.zerra.common.Reference;
import com.zerra.common.world.tile.Tile;
import com.zerra.common.world.tile.Tiles;

public class RenderingManager
{

	private TextureManager textureManager;
	private TextureMap textureMap;
	protected TileRenderer tileRenderer;
	protected GuiRenderer guiRenderer;
	protected Camera camera;
	protected Fbo fbo;

	public void init()
	{
		Display.createDisplay(Reference.NAME + " v" + Reference.VERSION, 1280, 720);
		Display.setIcon(new ResourceLocation("icons/16.png"), new ResourceLocation("icons/32.png"));
		I18n.setLanguage(new Locale("en", "us"));

		this.textureManager = new TextureManager();
		this.textureMap = new TextureMap(new ResourceLocation("atlas"), this.textureManager);

		Tile[] tiles = Tiles.getTiles();
		for (Tile tile : tiles)
		{
			this.textureMap.register(tile.getTexture());
		}
		this.textureMap.stitch();
		this.tileRenderer = new TileRenderer();
		this.guiRenderer = new GuiRenderer();
		this.camera = new Camera();
		this.fbo = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_RENDER_BUFFER, 2);
	}

	/**
	 * @return The texture manager for the game.
	 */
	public TextureManager getTextureManager()
	{
		return textureManager;
	}

	/**
	 * @return The texture map the game uses.
	 */
	public TextureMap getTextureMap()
	{
		return textureMap;
	}

	/**
	 * @return The camera for the client.
	 */
	public Camera getCamera()
	{
		return camera;
	}

	/**
	 * @return The FBO for the client.
	 */
	public Fbo getFbo()
	{
		return fbo;
	}

	/**
	 * @return The GUI renderer for the client.
	 */
	public GuiRenderer getGuiRenderer()
	{
		return guiRenderer;
	}

	/**
	 * @return The tile renderer for the client.
	 */
	public TileRenderer getTileRenderer()
	{
		return tileRenderer;
	}
}
