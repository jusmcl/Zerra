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

	public Map<String, Mod> loadedMods = new HashMap<>();
	public Map<Integer, Mod> modGroupOrder = new HashMap<>();

	public List<String> initializedMods = new ArrayList<>();

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
			} else
			{

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
