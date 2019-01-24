package com.zerra.common.network.msg;

import com.zerra.common.network.Message;
import com.zerra.common.world.World;
import simplenet.Client;
import simplenet.packet.Packet;

public class MessageBadRequest extends Message
{
	private String error;

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
		client.readString(s -> this.error = s);
	}

	@Override
	public void handle(World world)
	{
		//TODO
	}
}
