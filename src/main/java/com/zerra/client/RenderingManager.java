package com.zerra.client;

import com.zerra.client.gfx.renderer.GuiRenderer;
import com.zerra.client.gfx.renderer.tile.TileRenderer;
import com.zerra.client.gfx.texture.TextureManager;
import com.zerra.client.gfx.ui.text.FontManager;
import com.zerra.client.util.Fbo;
import com.zerra.client.util.ResourceLocation;
import com.zerra.client.view.Camera;
import com.zerra.client.view.Display;
import com.zerra.common.Reference;

public class RenderingManager
{

	private TextureManager textureManager;
	protected FontManager fontManager;
	protected TileRenderer tileRenderer;
	protected GuiRenderer guiRenderer;
	protected Camera camera;
	protected Fbo fbo;

	public void init()
	{
		Display.createDisplay(Reference.NAME + " v" + Reference.VERSION, 1280, 720);
		Display.setIcon(new ResourceLocation("icons/16.png"), new ResourceLocation("icons/32.png"));

		this.textureManager = new TextureManager();
		this.fontManager = new FontManager();
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

	/**
	 * @return The font manager for the client.
	 */
	public FontManager getFontManager()
	{
		return fontManager;
	}
}
