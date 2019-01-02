package com.zerra.common.world.storage.plate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.joml.Vector2i;
import org.joml.Vector3i;

import com.zerra.common.world.entity.Entity;
import com.zerra.common.world.storage.Layer;
import com.zerra.common.world.tile.Tile;
import com.zerra.common.world.tile.Tiles;

public class LayerPlate implements Layer {

	private static Random random = new Random();
	private List<Plate> loadedPlates = new ArrayList<>();

	@Override
	public Tile getTileAt(Vector2i position, int y) {
		Optional<Plate> optionalPlate = this.loadedPlates.stream().filter(plate -> plate.isInsidePlate(position, y)).findAny();
		Plate plate = optionalPlate.orElse(generate(new Vector3i(position.x / 100, y, position.y / 100)));
		return plate.getTileAt(position);
	}

	@Override
	public Plate[] getLoadedPlates() {
		return loadedPlates.toArray(new Plate[0]);
	}

	@Override
	public Entity[] getEntities() {
		return new Entity[0];
	}

	/**
	 * @deprecated placeholder for generating plates, just for testing purposes
	 */
	public Plate generate(Vector3i pos) {
		Plate plate = new Plate(100, this);
		plate.setPlatePos(pos);
		plate.fill(0, () -> random.nextInt(4) > 1 ? Tiles.STONE : Tiles.GRASS);
		this.loadedPlates.add(plate);
		return plate;
	}

	@Override
	public Plate getPlate(Vector3i position) {
		Optional<Plate> optionalPlate = this.loadedPlates.stream().filter(plate -> plate.getPlatePos().equals(position)).findAny();
		return optionalPlate.orElseGet(() -> generate(position));
	}
}
