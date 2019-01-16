package com.zerra.common.world.data;

import com.zerra.common.registry.RegistryNameable;
import com.zerra.common.world.World;
import com.zerra.common.world.storage.Storable;

public abstract class WorldData implements Storable, RegistryNameable {

	private String registryName;

	public WorldData(String name) {
		this.registryName = name;
	}

	@Override
	public void setDomain(String domain) {
		this.registryName = RegistryNameable.injectDomain(this.registryName, domain);
	}

	@Override
	public String getRegistryName() {
		return registryName;
	}

	/**
	 * Returns whether this {@link WorldData} should have an instance per {@link com.zerra.common.world.storage.Layer}
	 * or per {@link World}
	 *
	 * @return True if per {@link com.zerra.common.world.storage.Layer}, False if per {@link World}
	 */
	public boolean isPerLayer() {
		return false;
	}
}
