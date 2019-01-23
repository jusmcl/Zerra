package com.zerra.client.network;

import com.zerra.common.network.ConnectionManager;
import com.zerra.common.network.Message;
import com.zerra.common.network.msg.MessageConnect;
import com.zerra.common.world.World;
import simplenet.Client;

import java.util.UUID;

public class ClientConnectionManager extends ConnectionManager<Client>
{
	private UUID uuid;

	public ClientConnectionManager()
	{
		super(new Client());
	}

	public void setWorld(World world)
	{
		this.world = world;
	}

	public void connect()
	{
		connect(LOCALHOST, PORT);
	}

	public void connect(String address, int port)
	{
		receiver.connect(address, port);
	}

	@Override
	public void createListeners()
	{
		receiver.onConnect(() ->
		{
			LOGGER.info("Successfully connected to the server!");

			// TODO: Make this not random.
			this.uuid = UUID.randomUUID();
			this.sendToServer(new MessageConnect(uuid));
		});

		receiver.readIntAlways(id -> handleMessage(receiver, id));
	}

	@Override
	public void sendToServer(Message message)
	{
		message.prepare().writeAndFlush(receiver);
	}

	@Override
	public void sendToClient(Message message, Client client)
	{
		LOGGER.warn("Can't send to client from the client!");
	}

	@Override
	public void sendToAllClients(Message message)
	{
		LOGGER.warn("Can't send to clients from the client!");
	}

	public UUID getUUID()
	{
		return this.uuid;
	}
}
