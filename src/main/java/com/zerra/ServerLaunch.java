package com.zerra;

import com.zerra.common.util.ArgsBuilder;
import com.zerra.server.ZerraServer;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * The main starting point for the server.
 * 
 * @author Ocelot5836
 */
public class ServerLaunch
{
	/**
	 * The entry point for a server with no client.
	 * 
	 * @param args The arguments for the server.
	 */
	public static void main(String[] args)
	{
		ArgsBuilder.deserialize(args);
		new Thread(new ZerraServer(true), "Server").start();
	}
}