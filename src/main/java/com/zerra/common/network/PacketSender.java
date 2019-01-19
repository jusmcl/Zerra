package com.zerra.common.network;

import java.util.UUID;

import com.zerra.server.network.ServerPacketManager;

import simplenet.Client;

public class PacketSender
{

	Client client;
	ServerPacketManager server;

	public PacketSender(Client client)
	{
		this.client = client;
	}

	public PacketSender(ServerPacketManager server)
	{
		this.server = server;
	}

	public void sendToServer(Message msg)
	{
		msg.prepare().writeAndFlush(client);
	}

	public void sendToClient(Client client, Message msg)
	{
		msg.prepare().writeAndFlush(client);
	}

	public void sendToAllClients(Message msg)
	{
		for (UUID uuid : server.getClients().keySet())
		{
			msg.prepare().writeAndFlush(server.getClients().get(uuid));
		}
	}
}
