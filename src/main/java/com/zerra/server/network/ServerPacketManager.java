package com.zerra.server.network;

import simplenet.Server;

public class ServerPacketManager
{

	private Server server;

	public ServerPacketManager()
	{
		server = new Server();

		this.createListeners();
	}

	public void createListeners()
	{
		server.onConnect(client ->
		{
			System.out.println(client + " has connected!");

			client.readByteAlways(opcode ->
			{
				switch (opcode)
				{
				case 0:
					client.readString(System.out::println);
				}
			});

			// Register an optional pre-disconnection listener.
			client.preDisconnect(() -> System.out.println(client + " is about to disconnect!"));

			// Register an optional post-disconnection listener.
			client.postDisconnect(() -> System.out.println(client + " has successfully disconnected!"));
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
