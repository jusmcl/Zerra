package com.zerra.client.network;

import java.util.UUID;

import com.zerra.client.ZerraClient;
import com.zerra.common.network.PacketSender;

import simplenet.Client;
import simplenet.packet.Packet;

public class ClientPacketManager
{

	private Client client;
	private PacketSender sender;
	private UUID uuid;

	public ClientPacketManager()
	{
		client = new Client();
		sender = new PacketSender(client);

		this.createListeners();
	}

	public void switchToInternalServer()
	{
		if (client.getChannel().isOpen())
		{
			client.close();
		}
		client.connect("localhost", 43594);
	}

	public void switchToRemoteServer(String address, int port)
	{
		if (client.getChannel().isOpen())
		{
			client.close();
		}
		client.connect(address, port);
	}

	public void createListeners()
	{
		client.onConnect(() ->
		{
			System.out.println(client + " has connected to the server!");

			this.uuid = UUID.randomUUID();
			Packet.builder().putByte(0).putString(uuid.toString()).writeAndFlush(client);
		});

		client.preDisconnect(() -> ZerraClient.logger().info(client + " is about to disconnect from the server!"));

		client.postDisconnect(() -> ZerraClient.logger().info(client + " successfully disconnected from the server!"));
	}

	public void disconnect()
	{
		this.client.close();
	}

	public PacketSender getPacketSender()
	{
		return this.sender;
	}
}
