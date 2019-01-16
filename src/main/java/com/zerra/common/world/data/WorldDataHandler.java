package com.zerra.common.world.data;

import com.zerra.common.registry.Registries;
import com.zerra.common.registry.Registry;

import java.util.Set;

/**
 * Used by {@link com.zerra.common.world.World}s and {@link com.zerra.common.world.storage.Layer}s to get any
 * {@link WorldData} instances registered
 */
public class WorldDataHandler {

	public static Set<WorldData> getDataForWorld() {
		Registry<WorldData> registry = Registries.getRegistry(WorldData.class);
		return registry.get(entry -> !entry.isPerLayer());
	}

	public static Set<WorldData> getDataForLayer() {
		Registry<WorldData> registry = Registries.getRegistry(WorldData.class);
		return registry.get(WorldData::isPerLayer);
	}
}
