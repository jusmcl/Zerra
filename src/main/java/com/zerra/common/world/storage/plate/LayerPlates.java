package com.zerra.common.world.storage.plate;

import com.zerra.common.world.entity.Entity;
import com.zerra.common.world.storage.Layer;
import com.zerra.common.world.tile.Tile;
import org.joml.Vector2i;

import java.io.File;

public class LayerPlates implements Layer {

    Plate[] plates;

    @Override
    public Tile getTileAt(Vector2i position, int y) {
        return null;
    }

    @Override
    public Plate readFile(File file) {
        return null;
    }

    @Override
    public Plate[] getLoadedPlates() {
        return new Plate[0];
    }

    @Override
    public Entity[] getEntities() {
        return new Entity[0];
    }
}
