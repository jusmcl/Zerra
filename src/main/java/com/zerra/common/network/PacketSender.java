package com.zerra.common.network;

import simplenet.Client;
import simplenet.packet.Packet;

public class PacketSender
{

	Client client;

	public PacketSender(Client client)
	{
		this.client = client;
	}

	public void sendToServer(Packet packet)
	{
		Packet.builder().writeAndFlush(client);
	}

	public void sendToClient(Packet packet)
	{

	}

	public void sendToAllClients(Packet packet)
	{

	}
}
