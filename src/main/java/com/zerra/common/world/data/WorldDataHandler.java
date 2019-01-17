package com.zerra.common.world.data;

import com.zerra.common.registry.Registries;
import com.zerra.common.registry.Registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Used by {@link com.zerra.common.world.World}s and {@link com.zerra.common.world.storage.Layer}s to get any
 * {@link WorldData} instances registered
 */
public class WorldDataHandler {

	private static Map<String, WorldData> getData(Predicate<WorldDataFactory> predicate) {
		Registry<WorldDataFactory> registry = Registries.getRegistry(WorldDataFactory.class);
		Set<WorldData> data = registry.get(predicate, WorldDataFactory::getNewInstance);
		Map<String, WorldData> map = new HashMap<>();
		data.forEach(wd -> map.put(wd.getRegistryName(), wd));
		return map;
	}

	public static Map<String, WorldData> getDataForWorld() {
		return getData(factory -> !factory.isPerLayer());
	}

	public static Map<String, WorldData> getDataForLayer(int layer) {
		return getData(worldData -> worldData.isPerLayer() && worldData.isForLayer(layer));
	}
}
