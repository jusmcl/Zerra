package com.zerra.client.network;

import java.util.UUID;

import com.zerra.client.ZerraClient;
import com.zerra.common.network.ConnectionManager;
import com.zerra.common.network.Message;
import com.zerra.common.network.MessageSide;
import com.zerra.common.network.msg.MessageConnect;

import simplenet.Client;

public class ClientConnectionManager extends ConnectionManager<Client>
{
	public static final int MAX_BYTES = 10240;

	private UUID uuid;

	public ClientConnectionManager(ZerraClient zerra)
	{
		super(zerra, new Client(MAX_BYTES));
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
			this.sendToServer(new MessageConnect());
		});

		receiver.readIntAlways(id -> handleMessage(receiver, id));
	}

	@Override
	public void sendToServer(Message message)
	{
		prepareMessage(message).writeAndFlush(receiver);
	}

	@Override
	public void sendToClient(Message message, UUID uuid)
	{
		LOGGER.warn("Can't send to client from the client!");
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

	@Override
	public UUID getUUID()
	{
		return this.uuid;
	}

	@Override
	protected boolean isMessageSideValid(MessageSide side)
	{
		return side.isForServer();
	}
}
