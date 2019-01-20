package com.zerra.common.network.msg;

import com.zerra.common.network.Message;
import com.zerra.common.network.Opcodes;

import simplenet.packet.Packet;

public class MessagePing implements Message
{

	private long snapshot;

	public MessagePing()
	{
		this.snapshot = System.currentTimeMillis();
	}

	public MessagePing(long time)
	{
		this.snapshot = time;
	}

	@Override
	public Packet prepare()
	{
		return Packet.builder().putByte(Opcodes.CLIENT_PING).putLong(snapshot);
	}

}
