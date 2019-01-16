package com.zerra.api.mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.zerra.client.Zerra;
import com.zerra.common.util.MiscUtils;

/**
 * The class where mods loaded onto the classpath will be handled and managed.
 * 
 * @author Arpaesis
 */
public class ModManager
{

	public Map<String, Mod> loadedMods = new HashMap<>();
	public Map<String, ModIndex> modGroupOrder = new HashMap<>();

	public ModLoader loader;

	private ExecutorService pool;

	public ModManager()
	{
		loader = new ModLoader(this);
		pool = Executors.newCachedThreadPool();
	}

	public void process(String domain, int depth)
	{
		String[] dependencies = loadedMods.get(domain).getModInfo().getDependencies();

		if (!modGroupOrder.containsKey(domain))
		{
			modGroupOrder.put(domain, new ModIndex(depth));
		} else
		{
			// Check if this depth is lower than the current
			if (modGroupOrder.get(domain).getDepth() < depth)
			{
				modGroupOrder.put(domain, new ModIndex(depth));
			}
		}

		if (dependencies != null)
		{
			for (int i = 0; i < dependencies.length; i++)
			{
				this.process(dependencies[i], ++depth);
			}
		}
	}

	public void setupMods()
	{
		loader.loadMods("data/mods/");

		for (String modDomain : this.loadedMods.keySet())
		{
			this.process(modDomain, 0);
		}

		long snapshot = System.currentTimeMillis();
		initialize();
		Zerra.logger().info("Finished initializing mods in " + MiscUtils.secondsSinceTime(snapshot));
	}

	public void initialize()
	{
		int deepestLevel = 0;

		for (String domain : modGroupOrder.keySet())
		{
			if (modGroupOrder.get(domain).getDepth() > deepestLevel)
			{
				deepestLevel = modGroupOrder.get(domain).getDepth();
			}
		}

		List<Callable<?>> tasks = new ArrayList<>();

		for (int currentLevel = deepestLevel; currentLevel > -1; currentLevel--)
		{

			Zerra.logger().info("Loading mods on level " + currentLevel + "...");
			for (String domain2 : modGroupOrder.keySet())
			{
				if (modGroupOrder.get(domain2).getDepth() == currentLevel)
				{
					Callable<?> future = () ->
					{
						loadedMods.get(domain2).init(new ModInit(domain2));
						return true;
					};

					tasks.add(future);
				}
			}

			try
			{
				this.pool.invokeAll(tasks);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}

			tasks.clear();
		}

		this.pool.shutdown();
	}

	public boolean doesDomainExist(String domain)
	{
		return loadedMods.keySet().contains(domain);
	}
}
