package com.zerra.common.state;

import org.lwjgl.opengl.GL11;

import com.zerra.client.Zerra;
import com.zerra.client.gfx.renderer.GuiRenderer;
import com.zerra.client.view.Display;

public class WorldState extends State
{

	private Zerra zerra;

	public WorldState()
	{
		super("world");
		zerra = Zerra.getInstance();
		zerra.getPresence().setPresence("Playing on World \'" + zerra.getWorld().getName() + "\'", "512x512", "none");
	}

	@Override
	public void update()
	{
		zerra.getCamera().update();
		zerra.getInputHandler().updateGamepad();
	}

	@Override
	public void render()
	{
		zerra.getFbo().bindFrameBuffer();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		zerra.getTileRenderer().renderTiles(zerra.getCamera(), zerra.getWorld(), 0);
		zerra.getFbo().unbindFrameBuffer();

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, zerra.getFbo().getColorTexture(0));
		zerra.getGuiRenderer().setProjectionMatrix(GuiRenderer.FBO_MATRIX);
		zerra.getGuiRenderer().renderTextureQuad(0, 0, Display.getWidth(), Display.getHeight(), 0, 0, 1, 1, 1, 1);
		zerra.getGuiRenderer().restoreProjectionMatrix();
	}
}
