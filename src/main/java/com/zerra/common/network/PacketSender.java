package com.zerra.common.network;

import java.util.UUID;

import com.zerra.server.network.ServerPacketManager;

import simplenet.Client;
import simplenet.packet.Packet;

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

	public void sendToServer(Packet packet)
	{
		packet.writeAndFlush(client);
	}

	public void sendToClient(Client client, Packet packet)
	{
		packet.writeAndFlush(client);
	}

	public void sendToAllClients(Packet packet)
	{
		for (UUID uuid : server.getClients().keySet())
		{
			packet.writeAndFlush(server.getClients().get(uuid));
		}
	}
}
