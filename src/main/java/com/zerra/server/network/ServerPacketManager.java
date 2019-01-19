package com.zerra.server.network;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.zerra.common.network.Opcodes;
import com.zerra.common.network.PacketSender;
import com.zerra.server.ZerraServer;

import simplenet.Client;
import simplenet.Server;
import simplenet.packet.Packet;

public class ServerPacketManager
{

	private Server server;
	private PacketSender sender;
	private ConcurrentHashMap<UUID, Client> clients = new ConcurrentHashMap<>();

	public ServerPacketManager()
	{
		server = new Server();
		sender = new PacketSender(this);
		this.createListeners();
	}

	public void createListeners()
	{

		server.onConnect(client ->
		{
			ZerraServer.logger().info(client + " has connected!");

			client.readByteAlways(opcode ->
			{
				if (opcode == Opcodes.CLIENT_SHUTDOWN_INTERNAL_SERVER.value())
				{
					// A client shouldn't be able to shut down a remote server.
					if (!ZerraServer.getInstance().isCurrentlyRemote())
					{
						for (UUID uuid : clients.keySet())
						{
							clients.get(uuid).close();
						}
						this.server.close();
						ZerraServer.getInstance().stop();
					} else
					{
						this.sender.sendToClient(client, Packet.builder().putByte(Opcodes.ERROR_BAD_REQUEST.value()).putString("Client attempted to shut down a remote server."));
					}
				} else if (opcode == Opcodes.CLIENT_CONNECT.value())
				{
					client.readString(uuid ->
					{
						clients.put(UUID.fromString(uuid), client);
					});
				}
			});

			// Register an optional pre-disconnection listener.
			client.preDisconnect(() -> ZerraServer.logger().info(client + " is about to disconnect!"));

			// Register an optional post-disconnection listener.
			client.postDisconnect(() -> ZerraServer.logger().info(client + " has successfully disconnected from the server!"));
		});
	}

	public void bindInternally()
	{
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
