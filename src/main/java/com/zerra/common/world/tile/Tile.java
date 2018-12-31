package com.zerra.common.world.tile;

import com.zerra.common.world.storage.ILayer;
import org.joml.Vector2i;

public abstract class Tile {

    public abstract String getRegistryID();

    private final TileType tileType;

    public Tile(TileType type){
        this.tileType = type;
    }

    public TileType getTileType() {
        return tileType;
    }

    public float getHardness() {
        return tileType.getHardness();
    }

    public String getTypeName() {
        return tileType.getTypeName();
    }

    public String getToolType() {
        return tileType.getToolType();
    }

    public float getHarvestLevel() {
        return tileType.getHarvestLevel();
    }

    /**
     * gets called when an neighbour get's updated, for example, when a tile gets placed next to this tile.
     * @param ownPos position of this tile
     * @param neighbourPos
     * @param tileLayer
     */
    public void onNeighbourUpdate(Vector2i ownPos, Vector2i neighbourPos, ILayer tileLayer){

    }
}
