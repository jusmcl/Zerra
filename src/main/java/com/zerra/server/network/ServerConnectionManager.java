package com.zerra.server.network;

import java.util.Deque;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import javax.annotation.Nullable;

import com.zerra.common.network.ConnectionManager;
import com.zerra.common.network.Message;
import com.zerra.common.network.MessageSide;
import com.zerra.common.network.msg.MessageReady;
import com.zerra.common.network.msg.MessageTileData;
import com.zerra.server.ZerraServer;
import com.zerra.server.world.ServerWorld;

import simplenet.Client;
import simplenet.Server;
import simplenet.packet.Packet;

public class ServerConnectionManager extends ConnectionManager<Server>
{
	public static final int MAX_BYTES = 10240;
	
	private ConcurrentHashMap<UUID, Client> clients = new ConcurrentHashMap<>();
	private Deque<Client> queuedClients = new ConcurrentLinkedDeque<>();
	private boolean doneLoading;

	public ServerConnectionManager(ZerraServer zerra, @Nullable String address)
	{
		this(zerra, address == null ? LOCALHOST : address, PORT);
	}

	public ServerConnectionManager(ZerraServer zerra, String address, int port)
	{
		super(zerra, new Server(MAX_BYTES));

		boolean isLocalHost = LOCALHOST.equals(address);

		try
		{
			this.receiver.bind(address, port);
		}
		catch (RuntimeException e)
		{
			if (isLocalHost)
			{
				this.LOGGER.error(String.format("Unable to bind to %s:%s!", address, port), e);
			}
			else
			{
				this.LOGGER.warn("Unable to bind to {}:{}, falling back to localhost", address, port);
				this.receiver.bind(LOCALHOST, PORT);
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
		this.receiver.onConnect(client ->
		{
			this.queuedClients.add(client);
			client.readIntAlways(id ->
			{
				if (this.doneLoading)
				{
					this.queuedClients.forEach(queuedClient ->
					{
						this.sendToClient(new MessageReady(), queuedClient);
						this.sendToClient(new MessageTileData(((ServerWorld) this.zerra.getWorld()).getStorageManager().getTileIndexes()), queuedClient);
					});
					this.queuedClients.clear();
					handleMessage(client, id);
				}
			});
		});
	}

	@Override
	public void sendToServer(Message message)
	{
		LOGGER.warn("Can't send to server from the server!");
	}

	@Override
	public void sendToClient(Message message, UUID uuid)
	{
		sendToClient(message, this.clients.get(uuid));
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

	public void addClient(UUID uuid, Client client)
	{
		this.clients.put(uuid, client);
	}

	public void closeClient(UUID uuid)
	{
		Client client = this.clients.remove(uuid);
		if (this.queuedClients.contains(client))
		{
			this.queuedClients.remove(client);
		}
		if (client != null)
		{
			client.close();
		}
	}

	public void onFinishLoading()
	{
		this.doneLoading = true;
	}
}
