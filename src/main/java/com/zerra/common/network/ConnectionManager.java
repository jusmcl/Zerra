package com.zerra.common.network;

import com.zerra.common.registry.Registries;
import com.zerra.common.world.World;
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
		Message message = Registries.getMessage(messageId);
		if (message == null)
		{
			LOGGER.error("Unknown message: " + messageId);
		}
		else
		{
			//Read the data from the client and handle the message
			message.readFromClient(client);
			message.handle(world);
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
