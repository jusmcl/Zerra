package com.zerra.common.network;

import com.zerra.common.world.World;
import simplenet.Client;
import simplenet.packet.Packet;

import java.util.UUID;

public abstract class Message
{
	private Integer id = null;

	public Message() {}

	public final Packet prepare()
	{
		Packet packet = Packet.builder().putInt(this.hashCode());
		writeToPacket(packet);
		return packet;
	}

	public final Message setId(Integer id)
	{
		this.id = id;
		return this;
	}

	public final Integer getId()
	{
		return id;
	}

	/**
	 * Implement this and write any data to the {@link Packet}
	 *
	 * @param packet The packet to be sent
	 */
	protected abstract void writeToPacket(Packet packet);

	/**
	 * Implement this and read all data from the {@link Client}
	 *
	 * @param client Client to read data back from
	 */
	public abstract void readFromClient(Client client);

	/**
	 * Implement this and handle this message on the receiving end
	 *
	 * @param world The {@link World} instance
	 */
	public abstract void handle(World world);

	/*
		Helper methods
	 */

	protected final Packet putUuid(Packet packet, UUID uuid)
	{
		return packet.putLong(uuid.getMostSignificantBits()).putLong(uuid.getLeastSignificantBits());
	}

	protected final UUID readUuid(Client client)
	{
		final long[] uuidParts = new long[2];
		client.readLong(value -> uuidParts[0] = value);
		client.readLong(value -> uuidParts[1] = value);
		return new UUID(uuidParts[0], uuidParts[1]);
	}
}
