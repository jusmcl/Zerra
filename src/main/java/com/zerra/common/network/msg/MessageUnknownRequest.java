package com.zerra.common.network.msg;

import com.zerra.common.network.Message;
import com.zerra.common.network.MessageHandler;
import com.zerra.common.world.World;
import simplenet.Client;
import simplenet.packet.Packet;

public class MessageUnknownRequest extends Message
{
	private String error;

	public MessageUnknownRequest(String error)
	{
		this.error = error;
	}

	@Override
	protected void writeToPacket(Packet packet)
	{
		packet.putString(error);
	}

	@Override
	public void readFromClient(Client client)
	{
		client.readString(s -> this.error = s);
	}

	public static class Handler implements MessageHandler<MessageUnknownRequest>
	{
		@Override
		public void handleMessage(MessageUnknownRequest message, World world)
		{
			//TODO
		}
	}
}
