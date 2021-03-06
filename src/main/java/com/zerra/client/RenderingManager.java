package com.zerra.client;

import com.zerra.client.gfx.Camera;
import com.zerra.client.gfx.Display;
import com.zerra.client.gfx.renderer.GuiRenderer;
import com.zerra.client.gfx.renderer.entity.EntityRenderer;
import com.zerra.client.gfx.renderer.tile.TileRenderer;
import com.zerra.client.gfx.texture.TextureManager;
import com.zerra.client.util.Fbo;
import com.zerra.client.util.ResourceLocation;
import com.zerra.common.Reference;

public class RenderingManager
{
	private TextureManager textureManager;
	protected TileRenderer tileRenderer;
	protected EntityRenderer entityRenderer;
	protected GuiRenderer guiRenderer;
	protected Camera camera;
	protected Fbo fbo;

	public void init()
	{
		Display.createDisplay(Reference.NAME + " v" + Reference.VERSION, 1280, 720);
		Display.setIcon(new ResourceLocation("icons/16.png"), new ResourceLocation("icons/32.png"));

		this.textureManager = new TextureManager();
		this.tileRenderer = new TileRenderer();
		this.entityRenderer = new EntityRenderer();
		this.guiRenderer = new GuiRenderer();
		this.camera = new Camera();
		this.fbo = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_RENDER_BUFFER, 2);
	}

	/**
	 * Disposes of the shaders and other resources.
	 */
	public void dispose()
	{
		this.textureManager.dispose();
		this.tileRenderer.dispose();
		this.entityRenderer.dispose();
		this.guiRenderer.dispose();
		this.fbo.dispose();
	}

	/**
	 * @return The texture manager for the game.
	 */
	public TextureManager getTextureManager()
	{
		return textureManager;
	}

	/**
	 * @return The tile renderer for the client.
	 */
	public TileRenderer getTileRenderer()
	{
		return tileRenderer;
	}

	/**
	 * @return The entity renderer for the client
	 */
	public EntityRenderer getEntityRenderer()
	{
		return entityRenderer;
	}

	/**
	 * @return The GUI renderer for the client.
	 */
	public GuiRenderer getGuiRenderer()
	{
		return guiRenderer;
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
}
