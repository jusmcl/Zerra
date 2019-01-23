package com.zerra.common.network.msg;

import com.zerra.common.network.Message;
import com.zerra.common.network.MessageHandler;
import com.zerra.common.world.World;
import com.zerra.common.world.entity.Entity;
import org.joml.Vector3fc;
import simplenet.Client;
import simplenet.packet.Packet;

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

	@Override
	protected void writeToPacket(Packet packet)
	{
		putUuid(packet, this.entityUuid)
			.putFloat(entityPos.x()).putFloat(entityPos.y()).putFloat(entityPos.z())
			.putFloat(entityVel.x()).putFloat(entityVel.y()).putFloat(entityVel.z());
	}

	@Override
	public void readFromClient(Client client)
	{
		this.entityUuid = readUuid(client);
		//TODO: Finish
	}

	public static class Handler implements MessageHandler<MessageEntityMove>
	{
		@Override
		public void handleMessage(MessageEntityMove message, World world)
		{
			//TODO
		}
	}
}
