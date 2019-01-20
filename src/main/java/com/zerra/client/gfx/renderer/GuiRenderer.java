package com.zerra.client.gfx.renderer;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.zerra.client.ZerraClient;
import com.zerra.client.gfx.model.Model;
import com.zerra.client.gfx.shader.GuiShader;
import com.zerra.client.gfx.texture.map.TextureMapSprite;
import com.zerra.client.util.Loader;
import com.zerra.client.util.Maths;
import com.zerra.client.view.Display;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * The GUI renderer for the game. Used to render GUI onto the screen.
 *
 * @author Ocelot5836
 */
//TODO: Document this later, because wew lad this is gonna be painful.
public class GuiRenderer {

	public static final Matrix4f DEFAULT_MATRIX = new Matrix4f().ortho(0, Display.getWidth() / Renderer.SCALE, Display.getHeight() / Renderer.SCALE, 0, 0.3f, 1000.0f);
	public static final Matrix4f FBO_MATRIX = new Matrix4f().ortho(0, Display.getWidth(), 0, Display.getHeight(), 0.3f, 1000.0f);

	private static final Model QUAD = Loader.loadToVAO(new float[] { 0, 0, 1, 0, 0, 1, 1, 1 }, 2);

	private Matrix4f projectionMatrix;
	private GuiShader shader;

	public GuiRenderer() {
		this.projectionMatrix = new Matrix4f(DEFAULT_MATRIX);
		this.shader = new GuiShader();
		this.shader.start();
		this.shader.loadProjectionMatrix(this.projectionMatrix);
		this.shader.setColor(1, 1, 1, 1);
		this.shader.stop();
	}

	public void renderTextureQuad(float x, float y, float width, float height, TextureMapSprite sprite) {
		ZerraClient.getInstance().getTextureManager().bind(ZerraClient.getInstance().getTextureMap().getLocation());
		this.renderTextureQuad(x, y, width, height, 0, 0, sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight(), sprite.getAtlasWidth(), sprite.getAtlasHeight());
	}

	public void renderTextureQuad(float x, float y, float width, float height, float rotationX, float rotationY, TextureMapSprite sprite) {
		this.renderTextureQuad(x, y, width, height, rotationX, rotationY, sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight(), sprite.getAtlasWidth(), sprite.getAtlasHeight());
	}

	public void renderTextureQuad(float x, float y, float width, float height, float u, float v, float uWidth, float vHeight, float textureWidth, float textureHeight) {
		this.renderTextureQuad(x, y, width, height, 0, 0, u, v, uWidth, vHeight, textureWidth, textureHeight);
	}

	public void renderTextureQuad(float x, float y, float width, float height, float rotationX, float rotationY, float u, float v, float uWidth, float vHeight, float textureWidth, float textureHeight) {
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

	public void setProjectionMatrix(Matrix4f projectionMatrix) {
		this.shader.start();
		this.projectionMatrix = projectionMatrix;
		this.shader.loadProjectionMatrix(projectionMatrix);
		this.shader.stop();
	}

	public void restoreProjectionMatrix() {
		this.setProjectionMatrix(DEFAULT_MATRIX);
	}
}