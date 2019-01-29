package com.zerra.common.network.msg;

import java.util.UUID;

import org.joml.Vector3fc;

import com.zerra.common.Zerra;
import com.zerra.common.network.Message;
import com.zerra.common.world.World;
import com.zerra.common.world.entity.Entity;

import simplenet.Client;
import simplenet.packet.Packet;

public class MessageEntityMove extends Message
{
	private UUID entityUUID;
	private Vector3fc entityPos;
	private Vector3fc entityVel;

	public MessageEntityMove()
	{
	}

	public MessageEntityMove(Entity entity)
	{
		this.entityUUID = entity.getUUID();
		this.entityPos = entity.getPosition();
		this.entityVel = entity.getVelocity();
	}

	@Override
	protected void writeToPacket(Packet packet)
	{
		putUUID(packet, this.entityUUID);
		putVector3f(packet, this.entityPos);
		putVector3f(packet, this.entityVel);
	}

	@Override
	public void readFromClient(Client client)
	{
		this.entityUUID = readUUID(client);
		this.entityPos = readVector3f(client);
		this.entityVel = readVector3f(client);
	}

	@Override
	public Message handle(Zerra zerra, World world)
	{
		Entity entity = world.getEntityByUUID(this.entityUUID);
		if (entity != null)
		{
			entity.setPosition(this.entityPos);
			entity.setVelocity(this.entityVel);
			if (world.isServer())
			{
				zerra.getConnectionManager().sendToAllClients(new MessageEntityMove(entity));
			}
		}
		return null;
	}
}
