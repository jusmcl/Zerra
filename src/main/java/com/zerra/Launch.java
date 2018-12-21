package com.zerra;

import com.zerra.client.Zerra;

public class Launch {

	public static void main(String[] args) {
		if (args.length > 0 && args[0].equalsIgnoreCase("--server")) {
			new Thread(new Zerra(), "server").start();
		} else {
			new Thread(new Zerra(), "main").start();
		}
	}
}