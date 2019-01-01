package com.zerra.common.world.tile.impl;

import com.zerra.common.world.storage.Layer;
import com.zerra.common.world.tile.Tile;
import com.zerra.common.world.tile.TileType;

public class TileGround extends Tile {

    String name;

    int color;

    public TileGround(TileType type, int color, String name) {
        super(type);
        this.name = "zerra:tile_ground_" + name;
        this.color = color;
    }

    @Override
    public String getRegistryID() {
        return name;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public void spawnDropsInLayer(Layer layer) {

    }
}
