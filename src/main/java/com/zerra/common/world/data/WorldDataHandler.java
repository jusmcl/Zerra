package com.zerra.common.world.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import com.zerra.common.registry.Registries;
import com.zerra.common.registry.Registry;

/**
 * Used by {@link com.zerra.common.world.World}s and {@link com.zerra.common.world.storage.Layer}s to get any
 * {@link WorldData} instances registered
 */
public class WorldDataHandler {

	private static Map<String, WorldData> getData(Predicate<WorldDataFactory> predicate) {
		Registry<WorldDataFactory> registry = Registries.getRegistry(WorldDataFactory.class);
		Set<WorldData> data = registry.get(predicate, factory -> (WorldData) factory.getNewInstance());
		Map<String, WorldData> map = new HashMap<>();
		data.forEach(wd -> map.put(wd.getRegistryName(), wd));
		return map;
	}

	/**
	 * Gets new {@link WorldData} instances for a {@link com.zerra.common.world.World}
	 */
	public static Map<String, WorldData> getDataForWorld() {
		return getData(factory -> !factory.isPerLayer());
	}

	/**
	 * Gets new {@link WorldData} instances for a {@link com.zerra.common.world.storage.Layer}
	 */
	public static Map<String, WorldData> getDataForLayer(int layer) {
		return getData(worldData -> worldData.isPerLayer() && worldData.isForLayer(layer));
	}
}
