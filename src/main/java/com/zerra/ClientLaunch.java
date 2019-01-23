package com.zerra;

import com.zerra.client.ZerraClient;
import com.zerra.common.util.ArgsBuilder;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * The main starting point for the game.
 * 
 * @author Ocelot5836
 */
public class ClientLaunch
{
	/**
	 * Entry point for the game client.
	 * 
	 * @param args The arguments passed into the client.
	 */
	public static void main(String[] args)
	{
		ArgsBuilder.deserialize(args);
		new Thread(new ZerraClient(), "Client").start();
	}
}