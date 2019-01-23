package com.zerra.common.network.msg;

import com.zerra.common.network.Message;
import com.zerra.common.network.MessageHandler;
import com.zerra.common.world.World;
import simplenet.Client;
import simplenet.packet.Packet;

import java.util.UUID;

public class MessageConnect extends Message
{
	private UUID uuid;

	public MessageConnect(UUID uuid)
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

	public static class Handler implements MessageHandler<MessageConnect>
	{
		@Override
		public void handleMessage(MessageConnect message, World world)
		{
			//TODO
		}
	}
}
