package com.zerra.common.network.msg;

import com.zerra.common.Zerra;
import com.zerra.common.network.Message;
import com.zerra.common.network.MessageSide;
import com.zerra.common.world.World;

import simplenet.Client;
import simplenet.packet.Packet;

public class MessageReady extends Message
{
	@Override
	public MessageSide getReceivingSide()
	{
		return MessageSide.CLIENT;
	}

	@Override
	protected void writeToPacket(Packet packet)
	{
	}

	@Override
	public void readFromClient(Client client)
	{
	}

	@Override
	public Message handle(Zerra zerra, World world)
	{
		// TODO do things here like notify the client etc
		return null;
	}
}