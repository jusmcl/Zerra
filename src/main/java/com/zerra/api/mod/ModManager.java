package com.zerra.api.mod;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The class where mods loaded onto the classpath will be handled and managed.
 * 
 * @author Arpaesis
 */
public class ModManager
{

	public Map<String, Mod> loadedMods = new HashMap<>();
	public Map<Integer, Mod> modGroupOrder = new HashMap<>();

	public Set<String> initializedMods = new HashSet<>();

	public ModLoader loader;

	public ModManager()
	{
		loader = new ModLoader(this);
	}

	public void process(String domain)
	{

		String[] dependencies = loadedMods.get(domain).getModInfo().getDependencies();

		for (int i = 0; i < dependencies.length; i++)
		{

			boolean isInitialized = false;
			for (int j = 0; j < dependencies.length; j++)
			{
				if (initializedMods.contains(dependencies[j]))
				{
					isInitialized = true;
					break;
				}
			}

			if (!isInitialized)
			{
				this.process(dependencies[i]);

			}
		}

		loadedMods.get(domain).init();
		initializedMods.add(domain);
	}

	public void setupMods()
	{
		loader.loadMods("data/mods/");

		for (String modDomain : this.loadedMods.keySet())
		{
			Mod mod = this.loadedMods.get(modDomain);
			mod.init();
			mod.postInit();
		}

	}
}
