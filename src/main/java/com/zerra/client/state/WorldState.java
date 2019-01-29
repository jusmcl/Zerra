package com.zerra.client.state;

import com.zerra.client.ZerraClient;
import com.zerra.client.presence.PresenceBuilder;
import com.zerra.client.world.ClientWorld;
import com.zerra.common.network.msg.MessageDisconnect;
import com.zerra.server.ZerraServer;

public class WorldState extends State
{
	private ClientWorld world;

	protected ZerraClient zerraClient;

	public WorldState()
	{
		super("world");
	}

	@Override
	public PresenceBuilder setupPresence()
	{
		return new PresenceBuilder().setDetails("Playing on World '" + ZerraClient.getInstance().getWorld().getName() + "'").setLargeImage("zerra");
	}

	@Override
	public void init()
	{
		this.zerraClient = ZerraClient.getInstance();

		new Thread(new ZerraServer(false), "Server").start();

		this.world = ZerraClient.getInstance().createWorld("world", 1298428958710234L);

		try
		{
			Thread.sleep(1000);
		}
		catch (Exception e)
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
		this.world.render(partialTicks);
	}
}
