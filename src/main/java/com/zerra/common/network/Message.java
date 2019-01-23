package com.zerra.common.network;

import com.zerra.common.util.MiscUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import simplenet.Client;
import simplenet.packet.Packet;

import java.util.UUID;

public abstract class Message
{
	private String domain = null;
	private Integer id = null;

	public static <T extends Message> T create(Class<T> messageClass)
	{
		return MiscUtils.createNewInstance(messageClass);
	}

	public final Packet prepare()
	{
		Packet packet = Packet.builder().putInt(this.hashCode());
		writeToPacket(packet);
		return packet;
	}

	public final void setIdAndDomain(int id, String domain)
	{
		this.id = id;
		this.domain = domain;
	}

	public String getDomain()
	{
		return domain;
	}

	public Integer getId()
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

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(this.domain).append(this.id).toHashCode();
	}

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
