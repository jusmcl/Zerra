package com.zerra.api.mod;

import com.google.common.collect.Iterables;
import com.zerra.client.ZerraClient;
import com.zerra.common.util.MiscUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * The class where mods loaded onto the classpath will be handled and managed.
 *
 * @author Arpaesis
 * @author Bright_Spark
 */
public class ModManager
{

	public Map<String, Mod> loadedMods = new HashMap<>();
	public Map<String, Integer> modGroupOrder = new HashMap<>();
	private int deepestLevel = 0;

	public ModLoader loader;

	private ExecutorService pool;

	public ModManager()
	{
		this.loader = new ModLoader(this);
	}

	/**
	 * Loads mods, processes them one by one, and then cleans up the mod loading
	 * threads.
	 */
	public void setupMods()
	{
		this.loader.loadMods("data/mods/");

		for (String modDomain : this.loadedMods.keySet())
		{
			this.process(modDomain, 0);
		}

		this.pool = Executors.newCachedThreadPool(r -> new Thread(r, getClass().getSimpleName()));
		long snapshot = System.currentTimeMillis();
		this.initialize();
		ZerraClient.logger().info("Finished initializing mods in " + MiscUtils.secondsSinceTime(snapshot));
		this.pool.shutdown();
	}

	/**
	 * Takes a mod and processes it. Processing a mod is consists of giving the mod
	 * a load order based on its dependencies (mods should load after dependencies).
	 * 
	 * @param domain The domain of the mod.
	 * @param depth How high the mod should be prioritized. The bigger the number,
	 *        the larger the priority.
	 */
	private void process(String domain, final int depth)
	{
		if (depth > this.deepestLevel)
		{
			this.deepestLevel = depth;
		}

		this.modGroupOrder.compute(domain, (d, layer) -> layer == null ? depth : Math.max(layer, depth));

		String[] dependencies = this.loadedMods.get(domain).getModInfo().getDependencies();
		if (dependencies != null)
		{
			for (int i = 0; i < dependencies.length; i++)
			{
				this.process(dependencies[i], depth + 1);
			}
		}
	}

	/**
	 * Initializes all mods.
	 */
	private void initialize()
	{
		// Sorts the mods into easy-to-grab layers
		Map<Integer, Set<Mod>> layers = new HashMap<>();
		for (Map.Entry<String, Integer> modEntry : modGroupOrder.entrySet())
		{
			int layer = modEntry.getValue();
			Set<Mod> mods = layers.getOrDefault(layer, new HashSet<>());
			mods.add(loadedMods.get(modEntry.getKey()));
			layers.put(layer, mods);
		}

		for (int currentLevel = this.deepestLevel; currentLevel >= 0; currentLevel--)
		{
			Set<Mod> mods = layers.get(currentLevel);
			int size = mods.size();
			ZerraClient.logger().info("Loading {} mods on level {}...", size, currentLevel);

			if (size == 1)
			{
				Mod mod = Iterables.getOnlyElement(mods);
				mod.init(new ModInit(mod.getModInfo().getDomain()));
			} else
			{
				try
				{
					this.pool.invokeAll(mods.stream().map(mod -> (Callable<Void>) () ->
					{
						String d = mod.getModInfo().getDomain();
						mod.init(new ModInit(d));
						return null;
					}).collect(Collectors.toSet()));
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Verifies whether or not the domain of a mod exists within the currently
	 * loaded mods.
	 * 
	 * @param domain The domain to check for.
	 * @return Whether the domain exists or not.
	 */
	public boolean doesDomainExist(String domain)
	{
		return this.loadedMods.keySet().contains(domain);
	}
}
