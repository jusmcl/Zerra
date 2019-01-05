package com.zerra.common.world.storage.plate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.joml.Vector3i;

import com.zerra.common.world.entity.Entity;
import com.zerra.common.world.storage.Layer;

public class LayerPlate implements Layer {

	private static Random random = new Random();
	private Map<Vector3i, Plate> loadedPlates;

	public LayerPlate() {
		this.loadedPlates = new HashMap<Vector3i, Plate>();
	}

	@Override
	public void loadPlate() {

	}

	@Override
	public void unloadPlate() {

	}

	@Override
	public Plate[] getLoadedPlates() {
		return loadedPlates.values().toArray(new Plate[0]);
	}

	@Override
	public Entity[] getEntities() {
		return new Entity[0];
	}

	// public Plate generate(Vector3i pos) {
	// Plate plate = new Plate(this);
	// plate.setPlatePos(pos);
	// plate.fill(0, () -> random.nextInt(3) == 0 ? Tiles.STONE : random.nextInt(3) == 1 ? Tiles.GRASS : Tiles.SAND);
	// this.loadedPlates.add(plate);
	// return plate;
	// }

	@Override
	public Plate getPlate(Vector3i pos) {
		if (!this.isPlateLoaded(pos))
			this.loadPlate();
		return this.loadedPlates.get(pos);
	}

	@Override
	public boolean isPlateLoaded(Vector3i pos) {
		return this.loadedPlates.containsKey(pos);
	}
}
