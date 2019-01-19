package com.zerra.server.network;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.zerra.server.ZerraServer;

import simplenet.Client;
import simplenet.Server;

public class ServerPacketManager
{

	private Server server;
	private static ConcurrentHashMap<UUID, Client> clients = new ConcurrentHashMap<>();

	public ServerPacketManager()
	{
		server = new Server();

		this.createListeners();
	}

	public void createListeners()
	{

		server.onConnect(client ->
		{
			ZerraServer.logger().info(client + " has connected!");

			client.readByteAlways(opcode ->
			{
				switch (opcode)
				{
				case -1:
					for (UUID uuid : clients.keySet())
					{
						clients.get(uuid).close();
					}
					this.server.close();
					ZerraServer.getInstance().stop();
					break;

				case 0:
					client.readString(uuid ->
					{
						clients.put(UUID.fromString(uuid), client);
					});
					break;
				}
			});

			// Register an optional pre-disconnection listener.
			client.preDisconnect(() -> ZerraServer.logger().info(client + " is about to disconnect!"));

			// Register an optional post-disconnection listener.
			client.postDisconnect(() -> ZerraServer.logger().info(client + " has successfully disconnected from the server!"));
		});
	}

	public void bind()
	{
		server.bind("localhost", 43594);
	}

	public void close()
	{
		this.server.close();
	}
}
