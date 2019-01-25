package com.zerra.common.network.msg;

import com.zerra.common.Zerra;
import com.zerra.common.network.Message;
import com.zerra.common.network.MessageSide;
import com.zerra.common.world.World;
import com.zerra.common.world.entity.Entity;
import org.joml.Vector3fc;
import simplenet.Client;
import simplenet.packet.Packet;

import javax.annotation.Nonnull;
import java.util.UUID;

public class MessageEntityMove extends Message
{
	private UUID entityUuid;
	private Vector3fc entityPos;
	private Vector3fc entityVel;

	public MessageEntityMove(Entity entity)
	{
		this.entityUuid = entity.getUuid();
		this.entityPos = entity.getActualPosition();
		this.entityVel = entity.getVelocity();
	}

	@Nonnull
	@Override
	public MessageSide getReceivingSide()
	{
		return MessageSide.CLIENT;
	}

	@Override
	protected void writeToPacket(Packet packet)
	{
		putUuid(packet, this.entityUuid);
		putVector3f(packet, this.entityPos);
		putVector3f(packet, this.entityVel);
	}

	@Override
	public void readFromClient(Client client)
	{
		this.entityUuid = readUuid(client);
		this.entityPos = readVector3f(client);
		this.entityVel = readVector3f(client);
	}

	@Override
	public Message handle(Zerra zerra, World world)
	{
		Entity entity = world.getEntityByUUID(this.entityUuid);
		if (entity != null)
		{
			entity.setPosition(this.entityPos);
			entity.setVelocity(this.entityVel);
		}
		return null;
	}
}
