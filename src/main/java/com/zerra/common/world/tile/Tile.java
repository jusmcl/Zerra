package com.zerra.common.world.tile;

import com.zerra.common.world.entity.Entity;
import com.zerra.common.world.player.Player;
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

    /**
     * Called when te blayer is about to place a tile, can cancel the event if necessary
     *
     * @param pos the position of the tile
     * @param layer the layer in which the tile is placed
     * @return whether the player can place the block, and if it should be or not
     */
    public boolean canbePlaced(Vector2i pos, ILayer layer){
        return true;
    }


    /**
     * Called when a tile is about to be broken, checking whether the player can get drops from this tile or not
     * returning false won't cancel the breaking, it will only stop the player from getting drops
     * @param layer the layer in which the tile is broken
     * @param pos the position of the to be broken tile
     * @param player the player that is about to break this tile
     * @return whether the block's drops should be placed in the world
     */
    public boolean canBeHarvestedBy(ILayer layer, Vector2i pos, Player player){
        return true;
    }

    /**
     * Called when an entity is about to start breaking a block to check whether or not the entity can break this tile
     * Can cancel the event by returning false
     * @param layer the layer in which the tile is
     * @param pos the position of the to be broken tile
     * @param entity the breaking entity
     * @return true if the entity can break the tile, otherwise false
     */
    public boolean canBeBrokenBy(ILayer layer, Vector2i pos, Entity entity){
        return true;
    }


    public abstract int getColor();
}
