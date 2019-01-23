package com.zerra.common.network;

import com.zerra.common.registry.Registries;
import com.zerra.common.world.World;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import simplenet.Client;
import simplenet.channel.Channeled;
import simplenet.receiver.Receiver;

public abstract class ConnectionManager<T extends Receiver & Channeled>
{
	public static final String LOCALHOST = "localhost";
	public static final int PORT = 43594;
	protected final Logger LOGGER = LogManager.getLogger();
	protected T receiver;
	protected World world;

	public ConnectionManager(T receiver)
	{
		this.receiver = receiver;
		this.createListeners();
	}

	public void setWorld(World world)
	{
		this.world = world;
	}

	protected void handleMessage(Client client, int messageId)
	{
		Pair<Class<? extends Message>, MessageHandler<? extends Message>> pair = Registries.getMessage(messageId);
		if (pair == null)
		{
			LOGGER.error("Unknown message: " + messageId);
		}
		else
		{
			//Create the Message instance and read the data from the client
			Message message = Message.create(pair.getLeft());
			message.readFromClient(client);
			//Get the MessageHandler and handle the Message
			pair.getRight().handleMessage(message, world);
		}
	}

	public abstract void createListeners();

	public abstract void sendToServer(Message message);

	public abstract void sendToClient(Message message, Client client);

	public abstract void sendToAllClients(Message message);

	public void close()
	{
		receiver.close();
	}
}
