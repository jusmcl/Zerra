package com.zerra.common.world;

import javax.annotation.Nullable;

import com.zerra.common.world.storage.Layer;
import com.zerra.common.world.storage.plate.LayerPlate;

public class World {

	private final Layer[] layers;

	public World() {
		this.layers = new Layer[6];
		for (int i = 0; i < 6; i++) {
			layers[i] = new LayerPlate();
		}
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
}