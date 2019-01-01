package com.zerra.common.world.storage.plate;

import com.zerra.common.world.entity.Entity;
import com.zerra.common.world.storage.Layer;
import com.zerra.common.world.tile.Tile;
import org.joml.Vector2i;

import java.util.List;
import java.util.Optional;

public class LayerPlates implements Layer {

    List<Plate> loadedPlates;
    Plate dummy = new Plate(0, this);

    @Override
    public Tile getTileAt(Vector2i position, int y) {
        Optional<Plate> plate = loadedPlates.stream().filter(plate1 -> plate1.isInsidePlate(position)).findAny();
        if(!plate.isPresent()){
            plate = Optional.of(generate());
        }

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

    public Plate generate(){

        return null;
    }
}
