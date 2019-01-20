package com.zerra.client.state;

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

		new Thread(new ZerraServer(false), "Server").start();
		
		zerraServer = ZerraServer.getInstance();
		
		try
		{
			Thread.sleep(2000);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		zerraClient.getClientManager().switchToInternalServer();
	}

	@Override
	public void update()
	{
		
	}

	@Override
	public void render()
	{
		zerraClient.getFbo().bindFrameBuffer();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		//TODO: This null check shouldn't be necessary. Both the server and client should be ready to go before even considering rendering.
		if (zerraServer != null && zerraServer.isReady())
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
