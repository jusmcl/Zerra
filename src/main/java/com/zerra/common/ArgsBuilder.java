package com.zerra.common;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

import com.zerra.common.world.storage.IOManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zerra.Launch;

public class ArgsBuilder {

	public static final Logger LAUNCH = LogManager.getLogger("Launch");

	private final boolean isServer;
	private final boolean isClient;
	private final String username;
	private final String loginKey;

	public ArgsBuilder(boolean isServer, String username, String loginKey) {
		this.isServer = isServer;
		this.isClient = !isServer;
		this.username = username;
		this.loginKey = loginKey;
	}

	@SuppressWarnings("unused")
	public static ArgsBuilder deserialize(String[] args) {
		if (Launch.IS_DEVELOPMENT_BUILD) {
			LAUNCH.info("Launching from development environment");
		}

		if (args.length == 0) {
			if (Launch.IS_DEVELOPMENT_BUILD) {
				return new ArgsBuilder(false, "player", "null");
			} else {
				LAUNCH.fatal("Missing required parameters");
				System.exit(-1);
			}
		}
		boolean isServer = false;
		String username = null, loginKey = null;
		Iterator<String> iterator = Arrays.asList(args).iterator();
		while (iterator.hasNext()) {
			String value = iterator.next();
			switch (value) {
			case "--server":
				isServer = true;
				break;
			case "--client":
				break;
			case "--loginkey":
				if (!iterator.hasNext()) {
					throw new IllegalArgumentException("after --loginkey a login key should be specified");
				}
				String key = iterator.next();
				if (key.startsWith("--")) {
					throw new IllegalArgumentException("after --loginkey a login key should be specified");
				}
				loginKey = key;
				break;
			case "--username":
				if (!iterator.hasNext()) {
					throw new IllegalArgumentException("after --username a username should be specified");
				}
				String name = iterator.next();
				if (name.startsWith("--")) {
					throw new IllegalArgumentException("after --username a username should be specified");
				}
				username = name;
				break;
			case "--dir":
				if(!iterator.hasNext()){
					throw new IllegalArgumentException("after --dir a directory should be specified");
				}
				String path = iterator.next();
				if(path.startsWith("--")){
					throw new IllegalArgumentException("after --dir a directory should be specified");
				}
				File folder = new File(path);
				if(!folder.isDirectory()){
					throw new IllegalArgumentException("after --dir a directory should be specified");
				}
				IOManager.init(folder);
				break;
			default:
				break;
			}
		}
		if (username == null) {
			if (Launch.IS_DEVELOPMENT_BUILD || isServer) {
				username = "player";
			} else {
				LAUNCH.fatal("Username cannot be null");
				System.exit(-1);
			}
		}
		if (loginKey == null) {
			if (Launch.IS_DEVELOPMENT_BUILD || isServer) {
				loginKey = "null";
			} else {
				LAUNCH.fatal("Login key cannot be null");
				System.exit(-1);
			}
		}
		return new ArgsBuilder(isServer, username, loginKey);
	}

	public boolean isClient() {
		return isClient;
	}

	public boolean isServer() {
		return isServer;
	}

	public String getLoginKey() {
		return loginKey;
	}

	public String getUsername() {
		return username;
	}
}
