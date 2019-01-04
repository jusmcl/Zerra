package com.zerra.common;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

import com.zerra.common.world.storage.IOManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zerra.Launch;

/**
 * @author Tebreca
 * Class that takes the String[] given as arg and deserializes it into an object holding the launch args vice versa
 */
public class ArgsBuilder {

	//TODO: make a toString to make this class able to produce args

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

	/**
	 * @param args the args given by the runtime environment
	 * @return an new instance of the {@link ArgsBuilder} which contains the values of the parsed args
	 */
	@SuppressWarnings("unused")
	public static ArgsBuilder deserialize(String[] args) {
		//check if zerra is in a development environment
		if (Launch.IS_DEVELOPMENT_BUILD) {
			LAUNCH.info("Launching from development environment");
		}

		//checks if there are enough args, unless in development build, it'll exit with a negative exit code
		if (args.length == 0) {
			if (Launch.IS_DEVELOPMENT_BUILD) {
				return new ArgsBuilder(false, "player", "null");
			} else {
				LAUNCH.fatal("Missing required parameters");
				System.exit(CrashCodes.INVALID_ARGUMENT);
			}
		}
		//default assignments
		boolean isServer = false;
		String username = null, loginKey = null;
		Iterator<String> iterator = Arrays.asList(args).iterator();

		//iterating trough strings as args; to add args: just add another case statement to the switch.
		//then, when you need the next string, check if the iterator has a next arg, and that it doesn't start
		//with "--" for an example look at the case for loginKey, username and dir
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
				if(!folder.exists()){
					throw new IllegalArgumentException("after --dir an existing directory should be specified");
				}
				//instead of saving it, we preinit the io manager before we start zerra
				IOManager.init(folder);
				break;
			default:
				break;
			}
		}
		//test if all args are set, if not, assigning the data but nly if IS_DEVELOPMENT_BUILD is true
		if (username == null) {
			if (Launch.IS_DEVELOPMENT_BUILD || isServer) {
				username = "player";
			} else {
				LAUNCH.fatal("Username cannot be null");
				System.exit(CrashCodes.INVALID_ARGUMENT);
			}
		}
		if (loginKey == null) {
			if (Launch.IS_DEVELOPMENT_BUILD || isServer) {
				loginKey = "null";
			} else {
				LAUNCH.fatal("Login key cannot be null");
				System.exit(CrashCodes.INVALID_ARGUMENT);
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
