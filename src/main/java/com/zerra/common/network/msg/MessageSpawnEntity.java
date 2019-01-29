package com.zerra.common.network.msg;

import com.zerra.common.Zerra;
import com.zerra.common.network.Message;
import com.zerra.common.network.MessageSide;
import com.zerra.common.util.UBObjectWrapper;
import com.zerra.common.world.World;
import com.zerra.common.world.entity.Entity;

import simplenet.Client;
import simplenet.packet.Packet;

public class MessageSpawnEntity extends Message
{
	private UBObjectWrapper entityData;

	public MessageSpawnEntity()
	{
	}

	public MessageSpawnEntity(Entity entity)
	{
		this.entityData = entity.writeToUBO(new UBObjectWrapper());
	}

	@Override
	public MessageSide getReceivingSide()
	{
		return MessageSide.CLIENT;
	}

	@Override
	protected void writeToPacket(Packet packet)
	{
		putUBOBject(packet, this.entityData);
	}

	@Override
	public void readFromClient(Client client)
	{
		readUBObject(client, (object) ->
		{
			this.entityData = new UBObjectWrapper(object);
		});
	}

	@Override
	public Message handle(Zerra zerra, World world)
	{
		System.out.println("UBOBject:" + this.entityData);
		// TODO read from UBO
		return null;
	}
}
