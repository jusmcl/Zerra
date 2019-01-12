package com.zerra.api.mod;

import java.util.HashMap;
import java.util.Map;

/**
 * The class where mods loaded onto the classpath will be handled and managed.
 * 
 * @author Arpaesis
 */
public class ModManager
{

	public static Map<String, Mod> loadedMods = new HashMap<>();
	public static Map<Integer, Mod> modGroupOrder = new HashMap<>();

	public void process(String domain)
	{

		String[] dependencies = loadedMods.get(domain).modInfo().getDependencies();

		for (int i = 0; i < dependencies.length; i++)
		{
			this.process(dependencies[i]);

			if (!loadedMods.get(dependencies[i]).modInfo().isInitialized())
			{
				loadedMods.get(dependencies[i]).init();
				loadedMods.get(dependencies[i]).modInfo().setInitialized(true);
			}
		}

		loadedMods.get(domain).init();
		loadedMods.get(domain).modInfo().setInitialized(true);
	}

	public boolean isModInitialized(String domain)
	{

		return false;
	}
}
