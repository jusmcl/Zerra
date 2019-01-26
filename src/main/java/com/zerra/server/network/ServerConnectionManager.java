package com.zerra.server.network;

import com.zerra.common.network.ConnectionManager;
import com.zerra.common.network.Message;
import com.zerra.common.network.MessageSide;
import com.zerra.server.ZerraServer;
import simplenet.Client;
import simplenet.Server;
import simplenet.packet.Packet;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ServerConnectionManager extends ConnectionManager<Server>
{
	private ConcurrentHashMap<UUID, Client> clients = new ConcurrentHashMap<>();

	public ServerConnectionManager(ZerraServer zerra, @Nullable String address)
	{
		this(zerra, address == null ? LOCALHOST : address, PORT);
	}

	public ServerConnectionManager(ZerraServer zerra, String address, int port)
	{
		super(zerra, new Server());

		boolean isLocalHost = LOCALHOST.equals(address);

		try
		{
			receiver.bind(address, port);
		} catch (RuntimeException e)
		{
			if (isLocalHost)
			{
				LOGGER.error(String.format("Unable to bind to %s:%s!", address, port), e);
			} else
			{
				LOGGER.warn("Unable to bind to {}:{}, falling back to localhost", address, port);
				receiver.bind(LOCALHOST, PORT);
				isLocalHost = true;
			}
		}

		zerra.setCurrentlyRemote(!isLocalHost);
	}

	@Override
	public void createListeners()
	{
		// TODO: We need a way of adding to the clients map when we get a
		// MessageConnect!
		receiver.onConnect(client -> client.readIntAlways(id -> handleMessage(client, id)));
	}

	@Override
	public void sendToServer(Message message)
	{
		LOGGER.warn("Can't send to server from the server!");
	}

	@Override
	public void sendToClient(Message message, UUID uuid)
	{
		sendToClient(message, clients.get(uuid));
	}

	@Override
	public void sendToClient(Message message, Client client)
	{
		prepareMessage(message).writeAndFlush(client);
	}

	@Override
	public void sendToAllClients(Message message)
	{
		Packet packet = prepareMessage(message);
		getClients().values().forEach(packet::writeAndFlush);
	}

	@Override
	protected UUID getUUID()
	{
		return null;
	}

	@Override
	protected boolean isMessageSideValid(MessageSide side)
	{
		return side.isForClient();
	}

	private ConcurrentHashMap<UUID, Client> getClients()
	{
		return this.clients;
	}

    public boolean addClient(UUID uuid, Client client)
    {
        return clients.putIfAbsent(uuid, client) == null;
	}

	public void closeClient(UUID uuid)
	{
		Client client = clients.remove(uuid);
		if (client != null)
		{
			client.close();
		}
	}
}
