package com.zerra.api.mod;

import com.zerra.client.ZerraClient;
import com.zerra.common.Zerra;
import com.zerra.common.util.MiscUtils;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

import java.util.List;
import java.util.regex.Pattern;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * This loads all mods onto the classpath during initialization of the game.
 * 
 * @author Arpaesis
 */
public class ModLoader
{
	private static final Pattern DOMAIN_PATTERN = Pattern.compile("^\\w{3,}$");
	private ModManager modManager;

	public ModLoader(ModManager modManager)
	{
		this.modManager = modManager;
	}

	/**
	 * Loads the mods (jars) from the given directory.
	 * 
	 * @param directory The directory to search in.
	 */
	public void loadMods(String directory)
	{
		ZerraClient.logger().info("Started loading mods...");

		long snapshot = System.currentTimeMillis();

		String modInterface = "com.zerra.api.mod.Mod";

		try (ScanResult scanResult = new ClassGraph().enableAllInfo().scan())
		{
			List<Class<?>> widgetClasses = scanResult.getClassesImplementing(modInterface).loadClasses();

			for (Class<?> modClass : widgetClasses)
			{
				try
				{
					//TODO: Use MiscUtils.createInstance once the messages branch is merged
					Mod mod = (Mod) modClass.getConstructor().newInstance();
					String domain = mod.getModInfo().getDomain();
					if(!DOMAIN_PATTERN.matcher(domain).matches())
					{
						throw new RuntimeException(String.format("The domain name '%s' is invalid for the pattern '%s'", domain, DOMAIN_PATTERN.pattern()));
					}

					modManager.loadedMods.put(domain, mod);
				}
				catch(Exception e)
				{
					Zerra.logger().error("Failed to load mod " + modClass.getName(), e);
				}
			}
		}

		ZerraClient.logger().info("Finished loading mods in " + MiscUtils.secondsSinceTime(snapshot));

	}
}