package com.zerra.common.world.storage;

import org.joml.Vector2i;
import org.joml.Vector3i;

import com.zerra.common.world.entity.Entity;
import com.zerra.common.world.storage.plate.Plate;
import com.zerra.common.world.tile.Tile;

public interface Layer {

	Tile getTileAt(Vector2i position, int y);

	Plate[] getLoadedPlates();

	Entity[] getEntities();

	Plate getPlate(Vector3i pos);
}
