package com.zerra.client.state;

import org.lwjgl.opengl.GL11;

import com.zerra.client.ZerraClient;
import com.zerra.client.gfx.renderer.GuiRenderer;
import com.zerra.client.view.Display;
import com.zerra.server.ZerraServer;

public class WorldState extends State
{

	public WorldState()
	{
		super("world");

		new Thread(new ZerraServer(false), "Server").start();
		// TODO: The server/world is created here (line of code above), however, the
		// client world should also be created along with it.

		ZerraClient.getInstance().createWorld("world", 1298428958710234L);

		reloadServerInstance();

		try
		{
			Thread.sleep(2000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		zerraClient.getConnectionManager().switchToInternalServer();
	}

	@Override
	public void update()
	{
		ZerraClient.getInstance().getWorld().update();
	}

	@Override
	public void render()
	{
		zerraClient.getRenderingManager().getFbo().bindFrameBuffer();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		zerraClient.getRenderingManager().getTileRenderer().renderTiles(zerraClient.getRenderingManager().getCamera(), zerraClient.getWorld(), 0);

		zerraClient.getRenderingManager().getFbo().unbindFrameBuffer();

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, zerraClient.getRenderingManager().getFbo().getColorTexture(0));
		zerraClient.getRenderingManager().getGuiRenderer().setProjectionMatrix(GuiRenderer.FBO_MATRIX);
		zerraClient.getRenderingManager().getGuiRenderer().renderTextureQuad(0, 0, Display.getWidth(), Display.getHeight(), 0, 0, 1, 1, 1, 1);
		zerraClient.getRenderingManager().getGuiRenderer().restoreProjectionMatrix();
	}
}
