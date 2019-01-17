package com.zerra.common.state;

import org.lwjgl.opengl.GL11;

import com.zerra.client.ZerraClient;
import com.zerra.client.gfx.renderer.GuiRenderer;
import com.zerra.client.view.Display;
import com.zerra.server.ZerraServer;

public class WorldState extends State
{

	private ZerraServer zerraServer;
	private ZerraClient zerraClient;

	public WorldState()
	{
		super("world");
		zerraClient = ZerraClient.getInstance();
		zerraServer = ZerraServer.getInstance();
		//TODO: zerraClient.getPresence().setPresence("Playing on World \'" + zerraServer.getWorld().getName() + "\'", "512x512", "none");
	}

	@Override
	public void update()
	{
		zerraClient.getCamera().update();
		zerraClient.getInputHandler().updateGamepad();
	}

	@Override
	public void render()
	{
		zerraClient.getFbo().bindFrameBuffer();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		if (zerraServer.isReady())
		{
			zerraClient.getTileRenderer().renderTiles(zerraClient.getCamera(), zerraServer.getWorld(), 0);
		}
		zerraClient.getFbo().unbindFrameBuffer();

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, zerraClient.getFbo().getColorTexture(0));
		zerraClient.getGuiRenderer().setProjectionMatrix(GuiRenderer.FBO_MATRIX);
		zerraClient.getGuiRenderer().renderTextureQuad(0, 0, Display.getWidth(), Display.getHeight(), 0, 0, 1, 1, 1, 1);
		zerraClient.getGuiRenderer().restoreProjectionMatrix();
	}
}
