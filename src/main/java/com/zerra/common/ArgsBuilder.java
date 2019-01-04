package com.zerra.common;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zerra.Launch;
import com.zerra.common.world.storage.IOManager;

/**
 * @author Tebreca Class that takes the String[] given as arg and deserializes it into an object holding the launch args vice versa
 */
public class ArgsBuilder {

	// TODO: make a toString to make this class able to produce args

	public static final Logger LAUNCH = LogManager.getLogger("Launch");

	private final boolean isServer;
	private final boolean isClient;
	private final String username;
	private final String loginKey;
	private final File dataDirectory;

	public ArgsBuilder(boolean isServer, String username, String loginKey, File dataDirectory) {
		this.isServer = isServer;
		this.isClient = !isServer;
		this.username = username;
		this.loginKey = loginKey;
		this.dataDirectory = dataDirectory;
	}

	/**
	 * @param args
	 *            the args given by the runtime environment
	 * @return an new instance of the {@link ArgsBuilder} which contains the values of the parsed args
	 */
	@SuppressWarnings("unused")
	public static ArgsBuilder deserialize(String[] args) {
		// check if zerra is in a development environment
		if (Launch.IS_DEVELOPMENT_BUILD) {
			LAUNCH.info("Launching from development environment");
		}

		// checks if there are enough args, unless in development build, it'll exit with a negative exit code
		if (args.length == 0) {
			if (Launch.IS_DEVELOPMENT_BUILD) {
				return new ArgsBuilder(false, "player", "null", new File("data"));
			} else {
				LAUNCH.fatal("Missing required parameters");
				System.exit(-1);
			}
		}
		// default assignments
		boolean isServer = false;
		String username = null, loginKey = null;
		File dataDirectory = new File("data");
		Iterator<String> iterator = Arrays.asList(args).iterator();

		// iterating trough strings as args; to add args: just add another case statement to the switch.
		// then, when you need the next string, check if the iterator has a next arg, and that it doesn't start
		// with "--" for an example look at the case for loginKey, username and dir
		while (iterator.hasNext()) {
			String value = iterator.next();
			switch (value) {
			case "--server":
				isServer = true;
				break;
			case "--client":
				//TODO
				break;
			case "--id":
				if(!iterator.hasNext()) {
					throw new IllegalArgumentException("after --id a directory should be specified");
				}
				AccountProcessor proc = new AccountProcessor(iterator.next());
				proc.process();
				break;
			case "--dir":
				if (!iterator.hasNext()) {
					throw new IllegalArgumentException("after --dir a directory should be specified");
				}
				String path = iterator.next();
				if (path.startsWith("--")) {
					throw new IllegalArgumentException("after --dir a directory should be specified");
				}
				dataDirectory = new File(path);
				if (!dataDirectory.isDirectory()) {
					throw new IllegalArgumentException("after --dir a directory should be specified");
				}
				// instead of saving it, we preinit the io manager before we start zerra
				IOManager.init(dataDirectory);
				break;
			default:
				break;
			}
		}
		// test if all args are set, if not, assigning the data but nly if IS_DEVELOPMENT_BUILD is true
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
		return new ArgsBuilder(isServer, username, loginKey, dataDirectory);
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
	
	public File getDataDirectory() {
		return dataDirectory;
	}
}