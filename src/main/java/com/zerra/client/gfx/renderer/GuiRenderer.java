package com.zerra.client.gfx.renderer;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.zerra.client.ZerraClient;
import com.zerra.client.gfx.Display;
import com.zerra.client.gfx.model.Model;
import com.zerra.client.gfx.shader.GuiShader;
import com.zerra.client.gfx.texture.map.TextureMapSprite;
import com.zerra.client.util.Loader;
import com.zerra.client.util.Maths;
import com.zerra.client.util.ResourceLocation;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * The GUI renderer for the game. Used to render GUI onto the screen.
 *
 * @author Ocelot5836
 */
public class GuiRenderer
{
	/** Used as the matrix that is used if there is only standard rendering going on. */
	public static final Matrix4f DEFAULT_MATRIX = new Matrix4f().ortho(0, Display.getWidth() / Renderer.SCALE, Display.getHeight() / Renderer.SCALE, 0, 0.3f, 1000.0f);
	/** Used with the game FBO that gets rendered */
	public static final Matrix4f FBO_MATRIX = new Matrix4f().ortho(0, Display.getWidth(), 0, Display.getHeight(), 0.3f, 1000.0f);

	/** The model data that is rendered */
	private static final Model QUAD = Loader.loadToVAO(new float[] { 0, 0, 1, 0, 0, 1, 1, 1 }, 2);

	/** The current projection matrix */
	private Matrix4f projectionMatrix;
	/** The shader that is used for any GUI */
	private GuiShader shader;

	public GuiRenderer()
	{
		this.projectionMatrix = new Matrix4f(DEFAULT_MATRIX);
		this.shader = new GuiShader();
		this.shader.start();
		this.shader.loadProjectionMatrix(this.projectionMatrix);
		this.shader.setColor(1, 1, 1, 1);
		this.shader.stop();
	}

	/**
	 * Renders a textured quad at x, y with size width, height, rotation as 0, 0, and textured with the specified sprite.
	 * 
	 * @param x
	 *            The x position of the quad
	 * @param y
	 *            The y position of the quad
	 * @param width
	 *            The x size of the quad
	 * @param height
	 *            The y size of the quad
	 * @param sprite
	 *            The sprite that will be used to texture the quad
	 */
	public void renderTexturedQuad(float x, float y, float width, float height, TextureMapSprite sprite)
	{
		ZerraClient.getInstance().getRenderingManager().getTextureManager().bind(ZerraClient.getInstance().getRenderingManager().getTextureManager().getTextureMap().getLocation());
		this.renderTexturedQuad(x, y, width, height, 0, 0, sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight(), sprite.getAtlasWidth(), sprite.getAtlasHeight());
	}

	/**
	 * Renders a textured quad at x, y with size width, height, rotation as rotationX, rotationY, and textured with the specified sprite.
	 * 
	 * @param x
	 *            The x position of the quad
	 * @param y
	 *            The y position of the quad
	 * @param width
	 *            The x size of the quad
	 * @param height
	 *            The y size of the quad
	 * @param rotationX
	 *            The amount in degrees that the quad will be rotated in the x axis
	 * @param rotationY
	 *            The amount in degrees that the quad will be rotated in the y axis
	 * @param sprite
	 *            The sprite that will be used to texture the quad
	 */
	public void renderTexturedQuad(float x, float y, float width, float height, float rotationX, float rotationY, TextureMapSprite sprite)
	{
		this.renderTexturedQuad(x, y, width, height, rotationX, rotationY, sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight(), sprite.getAtlasWidth(), sprite.getAtlasHeight());
	}

	/**
	 * Renders a textured quad at x, y with size width, height, rotation as 0, 0, and textured with a custom texture. The custom texture uses u, v as x and y on the texture image, uWidth, uHeight as the width and height of the selection on the texture, and textureWidth, textureHeight to know the x and y size of the texture.
	 * 
	 * @param x
	 *            The x position of the quad
	 * @param y
	 *            The y position of the quad
	 * @param width
	 *            The x size of the quad
	 * @param height
	 *            The y size of the quad
	 * @param u
	 *            The x on the texture to start selection
	 * @param v
	 *            The v on the texture to start selection
	 * @param uWidth
	 *            The width of the selection on the texture
	 * @param vHeight
	 *            The height of the selection on the texture
	 * @param textureWidth
	 *            The width of the actual texture
	 * @param textureHeight
	 *            The height of the actual texture
	 */
	public void renderTexturedQuad(float x, float y, float width, float height, float u, float v, float uWidth, float vHeight, float textureWidth, float textureHeight)
	{
		this.renderTexturedQuad(x, y, width, height, 0, 0, u, v, uWidth, vHeight, textureWidth, textureHeight);
	}

	/**
	 * Renders a textured quad at x, y with size width, height, rotation as rotationX, rotationY, and textured with a custom texture. The custom texture uses u, v as x and y on the texture image, uWidth, uHeight as the width and height of the selection on the texture, and textureWidth, textureHeight to know the x and y size of the texture.
	 * 
	 * @param x
	 *            The x position of the quad
	 * @param y
	 *            The y position of the quad
	 * @param width
	 *            The x size of the quad
	 * @param height
	 *            The y size of the quad
	 * @param rotationX
	 *            The amount in degrees that the quad will be rotated in the x axis
	 * @param rotationY
	 *            The amount in degrees that the quad will be rotated in the y axis
	 * @param u
	 *            The x on the texture to start selection
	 * @param v
	 *            The v on the texture to start selection
	 * @param uWidth
	 *            The width of the selection on the texture
	 * @param vHeight
	 *            The height of the selection on the texture
	 * @param textureWidth
	 *            The width of the actual texture
	 * @param textureHeight
	 *            The height of the actual texture
	 */
	public void renderTexturedQuad(float x, float y, float width, float height, float rotationX, float rotationY, float u, float v, float uWidth, float vHeight, float textureWidth, float textureHeight)
	{
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		this.shader.start();
		this.shader.loadTransformationMatrix(Maths.createTransformationMatrix(x, y, rotationX, rotationY, width, height));
		this.shader.setTextureCoords(u / textureWidth, v / textureHeight, uWidth / textureWidth, vHeight / textureHeight);
		GL30.glBindVertexArray(QUAD.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, QUAD.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		this.shader.stop();
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	/**
	 * Renders a quad with a specific color at x, y with size width, height, and the color using red, green, blue values.
	 * 
	 * @param x
	 *            The x position of the quad
	 * @param y
	 *            The y position of the quad
	 * @param width
	 *            The x size of the quad
	 * @param height
	 *            The y size of the quad
	 * @param red
	 *            The amount of red (0-255)
	 * @param green
	 *            The amount of green (0-255)
	 * @param blue
	 *            The amount of blue (0-255)
	 */
	public void renderColoredQuad(float x, float y, float width, float height, float red, float green, float blue) 
	{
		ZerraClient.getInstance().getRenderingManager().getTextureManager().bind(new ResourceLocation("textures/blank.png"));
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		this.shader.start();
		this.shader.loadTransformationMatrix(Maths.createTransformationMatrix(x, y, 0, 0, width, height));
		this.shader.setTextureCoords(0f, 0f, 1f, 1f);
		this.shader.setColor(red / 255, green / 255, blue / 255, 1);
		GL30.glBindVertexArray(QUAD.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, QUAD.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		this.shader.setColor(1, 1, 1, 1);
		this.shader.stop();
		GL11.glDisable(GL11.GL_BLEND);
	}

	/**
	 * Sets the projection matrix that will be used to render with.
	 * 
	 * @param projectionMatrix
	 *            The projection matrix to use
	 */
	public void setProjectionMatrix(Matrix4f projectionMatrix)
	{
		this.shader.start();
		this.projectionMatrix = projectionMatrix;
		this.shader.loadProjectionMatrix(projectionMatrix);
		this.shader.stop();
	}

	/**
	 * Restores the default projection matrix as the rendering matrix.
	 */
	public void restoreDefaultProjectionMatrix()
	{
		this.setProjectionMatrix(DEFAULT_MATRIX);
	}
}