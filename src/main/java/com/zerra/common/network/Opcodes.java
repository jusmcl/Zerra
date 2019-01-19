package com.zerra.common.network;

public enum Opcodes
{

	CLIENT_SHUTDOWN_INTERNAL_SERVER(-1),
	CLIENT_CONNECT(0);

	private int value;

	Opcodes(int value)
	{
		this.value = value;
	}

	public int value()
	{
		return value;
	}
}
