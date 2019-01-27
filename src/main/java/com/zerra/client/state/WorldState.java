package com.zerra.client.state;

import com.zerra.client.ZerraClient;
import com.zerra.client.world.ClientWorld;
import com.zerra.common.network.msg.MessageDisconnect;
import com.zerra.server.ZerraServer;

public class WorldState extends State
{
	private ClientWorld world;

	public WorldState()
	{
		super("world");

		new Thread(new ZerraServer(false), "Server").start();

		this.world = ZerraClient.getInstance().createWorld("world", 1298428958710234L);

		reloadServerInstance();

		zerraClient.getConnectionManager().connect();
	}

	@Override
	public void update()
	{
		ZerraClient.getInstance().getWorld().update();
	}

	@Override
	public void render(float partialTicks)
	{
		this.world.render(partialTicks);
	}

	public static void cleanupWorldState()
	{
		ZerraClient.getInstance().getConnectionManager().sendToServer(new MessageDisconnect());

		if (ZerraClient.getInstance().getWorld() != null)
		{
			ZerraClient.getInstance().getWorld().stop();
		}
	}
}
