package com.zerra.common.world;

import javax.annotation.Nullable;

import org.joml.Vector3i;

import com.zerra.common.world.storage.IOManager.WorldStorageManager;
import com.zerra.common.world.storage.Layer;
import com.zerra.common.world.storage.plate.LayerPlate;

public class World {

	private String name;
	private final Layer[] layers;
	private WorldStorageManager storageManager;

	public World(String name) {
		this.name = name;
		this.layers = new Layer[6];
		for (int i = 0; i < 6; i++) {
			layers[i] = new LayerPlate();
		}
		this.storageManager = new WorldStorageManager(this);
	}

	// Temp
	public void save() {
		this.storageManager.writePlateSafe(0, this.layers[0].getPlate(new Vector3i()));
	}

	public String getName() {
		return name;
	}

	public Layer[] getLayers() {
		return layers;
	}

	@Nullable
	public Layer getLayer(int layer) {
		if (layer < 0 || layer >= this.layers.length)
			return null;
		return this.layers[layer];
	}

	public WorldStorageManager getStorageManager() {
		return storageManager;
	}
}