package com.zerra.common.network;

import com.zerra.common.Zerra;
import com.zerra.common.world.World;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector3i;
import org.joml.Vector3ic;
import simplenet.Client;
import simplenet.packet.Packet;

import javax.annotation.Nonnull;
import java.util.UUID;

public abstract class Message
{
	private Integer id = null;
	protected UUID senderUuid = null;

	public Message() {}

	/**
	 * Prepares this message to be send by writing the data into a new {@link Packet} and returning it
	 */
	public final Packet prepare()
	{
		Packet packet = Packet.builder().putInt(this.id);
		if (this.includesSender() && this.senderUuid != null)
		{
			putUuid(packet, this.senderUuid);
		}
		writeToPacket(packet);
		return packet;
	}

	/**
	 * Sets the unique ID for this message type
	 * This is called and set automatically
	 */
	public final Message setId(Integer id)
	{
		this.id = id;
		return this;
	}

	/**
	 * Gets the unique ID for this message type
	 */
	public final Integer getId()
	{
		return id;
	}

	/**
	 * Sets the sender UUID to the message
	 */
	public final Message setSender(UUID senderUuid)
	{
		this.senderUuid = senderUuid;
		return this;
	}

	/**
	 * Gets the sender UUID in this message if there is one
	 */
	public final UUID getSender()
	{
		return senderUuid;
	}

	/**
	 * Returns if this message should have a UUID attached from the client who sent it
	 */
	public boolean includesSender()
	{
		return false;
	}

	/**
	 * Returns the side that this message should be sent to
	 */
	@Nonnull
	public MessageSide getReceivingSide()
	{
		return MessageSide.BOTH;
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
	 * @param zerra The {@link Zerra} instance
	 * @param world The {@link World} instance
	 * @return null or a {@link Message} to reply back to the sender
	 */
	public abstract Message handle(Zerra zerra, World world);

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

	protected final Packet putVector3i(Packet packet, Vector3ic vector3ic)
	{
		return packet.putInt(vector3ic.x()).putInt(vector3ic.y()).putInt(vector3ic.z());
	}

	protected final Vector3ic readVector3i(Client client)
	{
		final int[] vectorParts = new int[3];
		client.readInt(value -> vectorParts[0] = value);
		client.readInt(value -> vectorParts[1] = value);
		client.readInt(value -> vectorParts[2] = value);
		return new Vector3i(vectorParts[0], vectorParts[1], vectorParts[2]);
	}

	protected final Packet putVector3f(Packet packet, Vector3fc vector3fc)
	{
		return packet.putFloat(vector3fc.x()).putFloat(vector3fc.y()).putFloat(vector3fc.z());
	}

	protected final Vector3fc readVector3f(Client client)
	{
		final float[] vectorParts = new float[3];
		client.readFloat(value -> vectorParts[0] = value);
		client.readFloat(value -> vectorParts[1] = value);
		client.readFloat(value -> vectorParts[2] = value);
		return new Vector3f(vectorParts[0], vectorParts[1], vectorParts[2]);
	}
}
