package com.zerra.common.network;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zerra.common.Zerra;
import com.zerra.common.network.msg.MessageConnect;
import com.zerra.common.registry.Registries;
import com.zerra.server.network.ServerConnectionManager;

import simplenet.Client;
import simplenet.channel.Channeled;
import simplenet.packet.Packet;
import simplenet.receiver.Receiver;

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
	 * Sets the message ID and client UUID as sender and returns the result of
	 * {@link Message#prepare()}
	 */
	protected Packet prepareMessage(Message message)
	{
		// Validate side message is meant for
		if (!isMessageSideValid(message.getReceivingSide()))
		{
			LOGGER.warn("Message {} was attempted to be sent even though the message is for {}!", message.getClass().getName(), message.getReceivingSide());
		}
		// Get message ID from registry
		Integer id = Registries.getMessageId(message.getClass());
		if (id == null)
		{
			throw new RuntimeException(String.format("Tried to send a message of type %s which is not registered!", message.getClass().getName()));
		}
		message.setId(id);
		// Attach UUID
		if (message.includesSender())
		{
			LOGGER.debug("Attaching UUID {} to message", getUUID());
			message.setSender(getUUID());
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
		} else
		{
			ClientWrapper clientWrapper = new ClientWrapper(client);
			// Read the data from the client and handle the message
			if (message.includesSender() && this instanceof ServerConnectionManager)
			{
				message.setSender(message.readUUID(clientWrapper));
			}
			LOGGER.debug("Handling message type {} from sender {}", message.getClass().getName(), message.getSender());

			// Special handling for a new connection
			if (message instanceof MessageConnect && this instanceof ServerConnectionManager)
			{
				if (((ServerConnectionManager) this).addClient(message.getSender(), client))
				{
					LOGGER.debug("Added new client connection {}", message.getSender());
				}
			}

			message.readFromClient(clientWrapper);
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

	protected abstract UUID getUUID();

	protected abstract boolean isMessageSideValid(MessageSide side);
}
