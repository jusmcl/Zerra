package com.zerra.client;

import org.lwjgl.opengl.GL11;

import com.zerra.client.gfx.renderer.GuiRenderer;
import com.zerra.client.view.Display;
import com.zerra.common.world.World;

public class WorldState extends State
{

	public WorldState(World world)
	{
		Zerra.getInstance().getPresence().setPresence("Playing on World \'" + world.getName() + "\'", "512x512", "none");
	}

	@Override
	public void update(Zerra zerra)
	{
		zerra.camera.update();
		zerra.inputHandler.updateGamepad();
	}

	@Override
	public void render(Zerra zerra)
	{
		zerra.fbo.bindFrameBuffer();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		zerra.tileRenderer.renderTiles(zerra.camera, zerra.world, 0);
		zerra.fbo.unbindFrameBuffer();

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, zerra.fbo.getColorTexture(0));
		zerra.guiRenderer.setProjectionMatrix(GuiRenderer.FBO_MATRIX);
		zerra.guiRenderer.renderTextureQuad(0, 0, Display.getWidth(), Display.getHeight(), 0, 0, 1, 1, 1, 1);
		zerra.guiRenderer.restoreProjectionMatrix();
	}
}
