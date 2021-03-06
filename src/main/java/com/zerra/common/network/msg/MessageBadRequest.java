package com.zerra.common.network.msg;

import com.zerra.common.Zerra;
import com.zerra.common.network.Message;
import com.zerra.common.world.World;

import simplenet.Client;
import simplenet.packet.Packet;

public class MessageBadRequest extends Message
{
	private String error;

	public MessageBadRequest()
	{
		this(null);
	}

	public MessageBadRequest(String error)
	{
		this.error = error;
	}

	@Override
	protected void writeToPacket(Packet packet)
	{
		packet.putString(this.error);
	}

	@Override
	public void readFromClient(Client client)
	{
		this.error = client.readString();
	}

	@Override
	public Message handle(Zerra zerra, World world)
	{
		Zerra.logger().warn("Bad request: " + this.error);
		return null;
	}
}
