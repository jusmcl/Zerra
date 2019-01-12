package com.zerra.api.mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

	public List<String> initializedMods = new ArrayList<>();

	public void process(String domain)
	{

		String[] dependencies = loadedMods.get(domain).getModInfo().getDependencies();

		for (int i = 0; i < dependencies.length; i++)
		{
			this.process(dependencies[i]);

			boolean isInitialized = false;
			for (String initializedMod : initializedMods)
			{
				if (dependencies[i].matches(initializedMod))
				{
					isInitialized = true;
					break;
				}
			}

			if (!isInitialized)
			{
				loadedMods.get(dependencies[i]).init();
				initializedMods.add(dependencies[i]);
			}
		}

		loadedMods.get(domain).init();
		initializedMods.add(domain);
	}
}
