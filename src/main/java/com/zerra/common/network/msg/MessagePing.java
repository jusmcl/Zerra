package com.zerra.common.network.msg;

import com.zerra.common.network.Message;
import com.zerra.common.network.MessageHandler;
import com.zerra.common.world.World;
import simplenet.Client;
import simplenet.packet.Packet;

public class MessagePing extends Message
{
	private long snapshot;

	public MessagePing()
	{
		this(System.currentTimeMillis());
	}

	public MessagePing(long time)
	{
		this.snapshot = time;
	}

	@Override
	protected void writeToPacket(Packet packet)
	{
		packet.putLong(this.snapshot);
	}

	@Override
	public void readFromClient(Client client)
	{
		client.readLong(value -> this.snapshot = value);
	}

	public static class Handler implements MessageHandler<MessagePing>
	{
		@Override
		public void handleMessage(MessagePing message, World world)
		{
			//TODO
		}
	}
}
