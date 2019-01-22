package com.zerra.server.network;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.zerra.common.network.Opcodes;
import com.zerra.common.network.PacketSender;
import com.zerra.common.network.msg.MessagePing;
import com.zerra.common.network.msg.MessageUnknownRequest;
import com.zerra.server.ZerraServer;

import simplenet.Client;
import simplenet.Server;

public class ServerConnectionManager
{

	private Server server;
	private PacketSender sender;
	private ConcurrentHashMap<UUID, Client> clients = new ConcurrentHashMap<>();

	public ServerConnectionManager()
	{
		sender = new PacketSender(this);
	}

	public void createListeners()
	{

		server.onConnect(client ->
		{
			client.readByteAlways(opcode ->
			{

				// When a client closes
				if (opcode == Opcodes.CLIENT_DISCONNECT)
				{
					// Close the client no matter what.
					client.close();

					client.readString(uuid ->
					{
						clients.remove(UUID.fromString(uuid));
						ZerraServer.logger().info("Player with UUID " + uuid + " has left the game.");
					});

					// A client shouldn't be able to shut down a remote server.
					if (!ZerraServer.getInstance().isCurrentlyRemote())
					{
						this.server.close();
						ZerraServer.getInstance().stop();
					}
				}

				// When a client (host or non host) connects to the server.
				else if (opcode == Opcodes.CLIENT_CONNECT)
				{
					client.readString(uuid ->
					{
						clients.put(UUID.fromString(uuid), client);
						ZerraServer.logger().info("Player with UUID " + uuid + " has joined the game.");
					});
					// For when a non host client leaves the server.
				} else if (opcode == Opcodes.CLIENT_PING)
				{
					client.readLong(time -> this.sender.sendToClient(client, new MessagePing(time)));
				} else
				{
					this.sender.sendToClient(client, new MessageUnknownRequest("The client made an unknown request! This should not happen."));
				}
			});
		});
	}

	public void bindInternally()
	{
		server = new Server();
		this.createListeners();
		server.bind("localhost", 43594);
		ZerraServer.getInstance().setCurrentlyRemote(false);
	}

	/**
	 * Try to bind to an address + port. If the server is by default remote (meaning
	 * it's a standalone server), do not fall back to an internal server. If the
	 * server is by default not remote (internal), it is a client + server combo and
	 * should fall back to an internal if this fails.
	 */
	public void bindRemotely(String address, int port)
	{

		if (ZerraServer.getInstance().isNaturallyRemote())
		{
			server.bind(address, port);
		} else
		{
			try
			{
				server.bind(address, port);
				ZerraServer.getInstance().setCurrentlyRemote(true);
			} catch (Exception e)
			{
				e.printStackTrace();
				ZerraServer.getInstance().setCurrentlyRemote(false);
			}
		}
	}

	public void close()
	{
		this.server.close();
		this.server = null;
	}

	public ConcurrentHashMap<UUID, Client> getClients()
	{
		return this.clients;
	}

	public PacketSender getSender()
	{
		return this.sender;
	}
}
