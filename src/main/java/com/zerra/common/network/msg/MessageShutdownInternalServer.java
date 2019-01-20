package com.zerra.common.network.msg;

import com.zerra.common.network.Message;
import com.zerra.common.network.Opcodes;

import simplenet.packet.Packet;

public class MessageShutdownInternalServer implements Message
{

	@Override
	public Packet prepare()
	{
		return Packet.builder().putByte(Opcodes.CLIENT_SHUTDOWN_INTERNAL_SERVER);
	}

}
