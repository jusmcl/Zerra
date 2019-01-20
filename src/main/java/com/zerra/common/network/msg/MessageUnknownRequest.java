package com.zerra.common.network.msg;

import com.zerra.common.network.Message;
import com.zerra.common.network.Opcodes;

import simplenet.packet.Packet;

public class MessageUnknownRequest implements Message
{

	private String error;

	public MessageUnknownRequest(String error)
	{
		this.error = error;
	}

	@Override
	public Packet prepare()
	{
		return Packet.builder().putByte(Opcodes.ERROR_BAD_REQUEST).putString(error);
	}

}
