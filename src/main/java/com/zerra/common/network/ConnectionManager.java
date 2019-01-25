package com.zerra.common.network;

import com.zerra.common.Zerra;
import com.zerra.common.registry.Registries;
import com.zerra.server.network.ServerConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import simplenet.Client;
import simplenet.channel.Channeled;
import simplenet.packet.Packet;
import simplenet.receiver.Receiver;

import java.util.UUID;

public abstract class ConnectionManager<T extends Receiver & Channeled>
{
	public static final String LOCALHOST = "localhost";
	public static final int PORT = 43594;
	protected final Logger LOGGER = LogManager.getLogger();
	protected T receiver;
	protected Zerra zerra;

	public ConnectionManager(Zerra zerra, T receiver)
	{
		this.zerra = zerra;
		this.receiver = receiver;
		this.createListeners();
	}

	/**
	 * Sets the message ID and client UUID as sender and returns the result of {@link Message#prepare()}
	 */
	protected Packet prepareMessage(Message message)
	{
		//Validate side message is meant for
		if (!isMessageSideValid(message.getReceivingSide()))
		{
			LOGGER.warn("Message {} was attempted to be sent even though the message is for {}!",
				message.getClass().getName(), message.getReceivingSide());
		}
		//Get message ID from registry
		Integer id = Registries.getMessageId(message.getClass());
		if (id == null)
		{
			throw new RuntimeException(String.format("Tried to send a message of type %s which is not registered!", message.getClass().getName()));
		}
		message.setId(id);
		//Attach UUID
		if (message.includesSender())
		{
			message.setSender(getUuid());
		}
		LOGGER.debug("Preparing message {}", message.getClass().getName());
		return message.prepare();
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
			if (message.includesSender() && this instanceof ServerConnectionManager)
			{
				message.setSender(message.readUuid(client));
			}
			LOGGER.debug("Handling message type {} from sender {}", message.getClass().getName(), message.getSender());
			message.readFromClient(client);
			zerra.handleMessage(message);
		}
	}

	public abstract void createListeners();

	public abstract void sendToServer(Message message);

	public abstract void sendToClient(Message message, UUID uuid);

	public abstract void sendToClient(Message message, Client client);

	public abstract void sendToAllClients(Message message);

	public void close()
	{
		receiver.close();
	}

	protected abstract UUID getUuid();

	protected abstract boolean isMessageSideValid(MessageSide side);
}
