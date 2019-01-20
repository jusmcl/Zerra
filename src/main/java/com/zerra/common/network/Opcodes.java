package com.zerra.common.network;

public class Opcodes
{

	public static byte ERROR_UNKNOWN_REQUEST = -3;
	public static byte ERROR_BAD_REQUEST = -2;
	public static byte CLIENT_DISCONNECT = -1;
	public static byte CLIENT_CONNECT = 0;
	public static byte CLIENT_CHAT_MESSAGE = 1;
	public static byte CLIENT_PING = 2;
}
