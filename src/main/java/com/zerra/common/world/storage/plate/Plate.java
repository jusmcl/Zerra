package com.zerra.common.world.storage.plate;

import com.zerra.common.world.storage.Layer;
import com.zerra.common.world.tile.Tile;
import org.joml.Vector2i;

public class Plate {

    private Tile[][] tiles;

    Vector2i platePos;

    int size;

    Layer layerIn;

    public Plate(int size, Layer layer){
        this.tiles = new Tile[size][size];
        this.size = size - 1;
        this.layerIn = layer;
    }

    public void setPlatePos(Vector2i platePos) {
        this.platePos = platePos;
    }

    public Vector2i getPlatePos() {
        return platePos;
    }

    public boolean isInsidePlate(Vector2i tilePos){
        int x = tilePos.x / 100;
        int y = tilePos.y / 100;
        return platePos.x == x && platePos.y == y;
    }

    public int getSize() {
        return size;
    }

    public Layer getLayerIn() {
        return layerIn;
    }

    public Tile getTileAt(Vector2i position){
        int x = position.x % size;
        int z = position.y % size;
        return tiles[x][z];
    }

    public void setTileAt(Vector2i position, Tile toPlace){
        int x = position.x % size;
        int z = position.y % size;
        Tile toReplace = getTileAt(position);
        if(toPlace != Tile.NONE){
            toReplace.spawnDropsInLayer(layerIn);
        }
        this.tiles[x][z] = toPlace;
    }

}
