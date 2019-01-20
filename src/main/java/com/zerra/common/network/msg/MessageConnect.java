package com.zerra.common.network.msg;

import com.zerra.common.network.Message;
import com.zerra.common.network.Opcodes;

import simplenet.packet.Packet;

public class MessageConnect implements Message
{

	String uuid;

	public MessageConnect(String uuid)
	{
		this.uuid = uuid;
	}

	@Override
	public Packet prepare()
	{
		return Packet.builder().putByte(Opcodes.CLIENT_CONNECT).putString(uuid);
	}

}
