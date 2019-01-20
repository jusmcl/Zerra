package com.zerra.common.network.msg;

import org.joml.Vector3fc;

import com.zerra.common.network.Message;
import com.zerra.common.network.Opcodes;
import com.zerra.common.world.entity.Entity;

import simplenet.packet.Packet;

public class MessageEntityMove implements Message
{

	private Vector3fc entityPos;
	private Vector3fc entityVel;

	public MessageEntityMove(Entity entity)
	{
		this(entity.getActualPosition(), entity.getVelocity());
	}

	public MessageEntityMove(Vector3fc entityPos, Vector3fc entityVel)
	{
		this.entityPos = entityPos;
		this.entityVel = entityVel;
	}

	@Override
	public Packet prepare()
	{
		return Packet.builder().putByte(Opcodes.ENTITY_MOVE).putFloat(entityPos.x()).putFloat(entityPos.y()).putFloat(entityPos.z()).putFloat(entityVel.x()).putFloat(entityVel.y())
				.putFloat(entityVel.z());
	}

}
