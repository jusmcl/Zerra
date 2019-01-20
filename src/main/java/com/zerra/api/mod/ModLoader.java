package com.zerra.api.mod;

import java.util.List;

import com.zerra.client.ZerraClient;
import com.zerra.common.util.MiscUtils;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * This loads all mods onto the classpath during initialization of the game.
 * 
 * @author Arpaesis
 */
public class ModLoader
{
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
				Mod mod = (Mod) modClass.getConstructor().newInstance();

				if (mod != null)
				{
					modManager.loadedMods.put(mod.getModInfo().getDomain(), mod);
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		ZerraClient.logger().info("Finished loading mods in " + MiscUtils.secondsSinceTime(snapshot));

	}
}