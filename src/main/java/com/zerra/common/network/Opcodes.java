package com.zerra.common.network;

public enum Opcodes
{

	ERROR_BAD_REQUEST(-2),
	CLIENT_SHUTDOWN_INTERNAL_SERVER(-1),
	CLIENT_CONNECT(0);

	private byte value;

	Opcodes(int value)
	{
		this.value = (byte)value;
	}

	public byte value()
	{
		return value;
	}
}
