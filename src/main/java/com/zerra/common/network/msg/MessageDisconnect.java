package com.zerra.common.network.msg;

import com.zerra.common.network.Message;
import com.zerra.common.world.World;
import simplenet.Client;
import simplenet.packet.Packet;

import java.util.UUID;

public class MessageDisconnect extends Message
{
	private UUID uuid;

	public MessageDisconnect(UUID uuid)
	{
		this.uuid = uuid;
	}

	@Override
	protected void writeToPacket(Packet packet)
	{
		putUuid(packet, this.uuid);
	}

	@Override
	public void readFromClient(Client client)
	{
		this.uuid = readUuid(client);
	}

	@Override
	public void handle(World world)
	{
		//TODO
	}
}
