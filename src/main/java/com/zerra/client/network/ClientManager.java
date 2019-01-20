package com.zerra.client.network;

import java.util.UUID;

import com.zerra.client.ZerraClient;
import com.zerra.common.network.Opcodes;
import com.zerra.common.network.PacketSender;
import com.zerra.common.network.msg.MessageConnect;

import simplenet.Client;

public class ClientManager
{

	private Client client;
	private PacketSender sender;
	private UUID uuid;

	public ClientManager()
	{
		client = new Client();
		sender = new PacketSender(client);

		this.createListeners();
	}

	public void switchToInternalServer()
	{
		client.connect("localhost", 43594);
	}

	public void switchToRemoteServer(String address, int port)
	{
		client.connect(address, port);
	}

	public void createListeners()
	{
		client.onConnect(() ->
		{
			ZerraClient.logger().info("Successfully connected to the server!");

			// TODO: Make this not random.
			this.uuid = UUID.randomUUID();
			this.sender.sendToServer(new MessageConnect(uuid.toString()));
		});

		client.readByteAlways(opcode ->
		{
			if (opcode == Opcodes.ERROR_BAD_REQUEST)
			{
				client.readString(msg -> ZerraClient.logger().warn("The client made a bad request: " + msg));
			} else if (opcode == Opcodes.CLIENT_PING)
			{
				client.readLong(time -> ZerraClient.logger().info("Ping: " + (System.currentTimeMillis() - time) + "ms"));
			} else if (opcode == Opcodes.ERROR_UNKNOWN_REQUEST)
			{
				client.readString(msg -> ZerraClient.logger().warn(msg));
			}
		});
	}

	public void disconnect()
	{
		this.client.close();
	}

	public PacketSender getPacketSender()
	{
		return this.sender;
	}

	public UUID getUUID()
	{
		return this.uuid;
	}
}
