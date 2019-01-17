package com.zerra.api.mod;

import com.google.common.collect.Iterables;
import com.zerra.client.Zerra;
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
 * The class where mods loaded onto the classpath will be handled and managed.
 *
 * @author Arpaesis
 */
public class ModManager {

	public Map<String, Mod> loadedMods = new HashMap<>();
	public Map<String, Integer> modGroupOrder = new HashMap<>();
	private int deepestLevel = 0;

	public ModLoader loader;

	private ExecutorService pool;

	public ModManager() {
		this.loader = new ModLoader(this);
	}

	public void setupMods() {
		this.loader.loadMods("data/mods/");

		for (String modDomain : this.loadedMods.keySet()) {
			this.process(modDomain, 0);
		}

		this.pool = Executors.newCachedThreadPool();
		long snapshot = System.currentTimeMillis();
		this.initialize();
		Zerra.logger().info("Finished initializing mods in " + MiscUtils.secondsSinceTime(snapshot));
		this.pool.shutdown();
	}

	private void process(String domain, final int depth) {
		if (depth > this.deepestLevel) {
			this.deepestLevel = depth;
		}

		this.modGroupOrder.compute(domain, (d, layer) -> layer == null ? depth : Math.max(layer, depth));

		String[] dependencies = this.loadedMods.get(domain).getModInfo().getDependencies();
		if (dependencies != null) {
			for (int i = 0; i < dependencies.length; i++) {
				this.process(dependencies[i], depth + 1);
			}
		}
	}

	private void initialize() {
		//Sorts the mods into easy-to-grab layers
		Map<Integer, Set<Mod>> layers = new HashMap<>();
		for (Map.Entry<String, Integer> modEntry : modGroupOrder.entrySet()) {
			int layer = modEntry.getValue();
			Set<Mod> mods = layers.getOrDefault(layer, new HashSet<>());
			mods.add(loadedMods.get(modEntry.getKey()));
			layers.put(layer, mods);
		}

		for (int currentLevel = this.deepestLevel; currentLevel >= 0; currentLevel--) {
			Set<Mod> mods = layers.get(currentLevel);
			int size = mods.size();
			Zerra.logger().info("Loading {} mods on level {}...", size, currentLevel);

			if (size == 1) {
				Mod mod = Iterables.getOnlyElement(mods);
				mod.init(new ModInit(mod.getModInfo().getDomain()));
			} else {
				try {
					this.pool.invokeAll(mods.stream().map(mod -> (Callable<Void>) () -> {
						String d = mod.getModInfo().getDomain();
						mod.init(new ModInit(d));
						return null;
					}).collect(Collectors.toSet()));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean doesDomainExist(String domain) {
		return this.loadedMods.keySet().contains(domain);
	}
}
