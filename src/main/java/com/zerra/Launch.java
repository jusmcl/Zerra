package com.zerra;

import com.zerra.client.Zerra;
import com.zerra.common.ArgsBuilder;
import com.zerra.server.ZerraServer;

public class Launch {

    public static final String NAME = "Zerra";
    public static final String VERSION = "0.0.2";
    public static final String DOMAIN = "zerra";
    public static final boolean IS_DEVELOPMENT_BUILD = true;
    
	public static void main(String[] args) {
		ArgsBuilder builder = ArgsBuilder.deserialize(args);
		if (builder.isServer()) {
			new Thread(new ZerraServer(), "server").start();
		} else {
			new Thread(new Zerra(), "main").start();
		}
	}
}