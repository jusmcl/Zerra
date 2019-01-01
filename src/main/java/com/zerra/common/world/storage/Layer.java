package com.zerra.common.world.storage;

import com.zerra.common.world.entity.Entity;
import com.zerra.common.world.storage.plate.Plate;
import com.zerra.common.world.tile.Tile;
import org.joml.Vector2i;

import java.io.File;

public interface Layer {

    Tile getTileAt(Vector2i position, int y);

    Plate[] getLoadedPlates();

    Entity[] getEntities();

}
