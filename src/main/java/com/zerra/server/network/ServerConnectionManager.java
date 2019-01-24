package com.zerra.server.network;

import com.zerra.common.network.ConnectionManager;
import com.zerra.common.network.Message;
import com.zerra.server.ZerraServer;
import simplenet.Client;
import simplenet.Server;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ServerConnectionManager extends ConnectionManager<Server>
{
	private ConcurrentHashMap<UUID, Client> clients = new ConcurrentHashMap<>();

	public ServerConnectionManager()
	{
		this(LOCALHOST);
	}

	public ServerConnectionManager(String address)
	{
		this(address == null ? LOCALHOST : address, PORT);
	}

	public ServerConnectionManager(String address, int port)
	{
		super(new Server());

		boolean isLocalHost = LOCALHOST.equals(address);

		try
		{
			receiver.bind(address, port);
		}
		catch(RuntimeException e)
		{
			if (isLocalHost)
			{
				LOGGER.error(String.format("Unable to bind to %s:%s!", address, port), e);
			}
			else
			{
				LOGGER.warn("Unable to bind to {}:{}, falling back to localhost", address, port);
				receiver.bind(LOCALHOST, PORT);
				isLocalHost = true;
			}
		}

		ZerraServer.getInstance().setCurrentlyRemote(!isLocalHost);
	}

	@Override
	public void createListeners()
	{
		receiver.onConnect(client -> client.readIntAlways(id -> handleMessage(client, id)));
	}

	@Override
	public void sendToServer(Message message)
	{
		LOGGER.warn("Can't send to server from the server!");
	}

	@Override
	public void sendToClient(Message message, Client client)
	{
		message.prepare().writeAndFlush(client);
	}

	@Override
	public void sendToAllClients(Message message)
	{
		getClients().values().forEach(client -> message.prepare().writeAndFlush(client));
	}

	private ConcurrentHashMap<UUID, Client> getClients()
	{
		return this.clients;
	}
}
