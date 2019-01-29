package com.zerra.client.state;

import org.lwjgl.opengl.GL11;

import com.zerra.client.ZerraClient;
import com.zerra.client.gfx.renderer.GuiRenderer;
import com.zerra.client.view.Display;
import com.zerra.common.network.msg.MessageDisconnect;
import com.zerra.server.ZerraServer;

public class WorldState extends State
{

	protected ZerraClient zerraClient;

	public WorldState()
	{
		super("world");
	}

	@Override
	public void init()
	{
		this.zerraClient = ZerraClient.getInstance();

		new Thread(new ZerraServer(false), "Server").start();

		// TODO: Pass in a seed here.
		ZerraClient.getInstance().createWorld("world", 1298428958710234L);

		try
		{
			Thread.sleep(1000);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		zerraClient.getConnectionManager().connect();
	}

	@Override
	public void cleanState()
	{
		ZerraClient.getInstance().getConnectionManager().sendToServer(new MessageDisconnect());

		if (ZerraClient.getInstance().getWorld() != null)
		{
			ZerraClient.getInstance().getWorld().stop();
		}
	}

	@Override
	public void update()
	{
		ZerraClient.getInstance().getWorld().update();
	}

	@Override
	public void render(double mouseX, double mouseY, float partialTicks)
	{
		zerraClient.getRenderingManager().getFbo().bindFrameBuffer();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		zerraClient.getRenderingManager().getTileRenderer().renderTiles(zerraClient.getRenderingManager().getCamera(), zerraClient.getWorld(), 0);

		zerraClient.getRenderingManager().getFbo().unbindFrameBuffer();

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, zerraClient.getRenderingManager().getFbo().getColorTexture(0));
		zerraClient.getRenderingManager().getGuiRenderer().setProjectionMatrix(GuiRenderer.FBO_MATRIX);
		zerraClient.getRenderingManager().getGuiRenderer().renderTexturedQuad(0, 0, Display.getWidth(), Display.getHeight(), 0, 0, 1, 1, 1, 1);
		zerraClient.getRenderingManager().getGuiRenderer().restoreDefaultProjectionMatrix();
	}
}
