package com.zerra.common.world.storage.plate;

import com.zerra.common.world.storage.Layer;
import com.zerra.common.world.tile.Tile;
import org.joml.Vector2i;
import org.joml.Vector3i;

import java.util.function.Supplier;

public class Plate {

    private Tile[][] tiles;

    Vector3i platePos;

    int size;

    Layer layerIn;

    public Plate(int size, Layer layer){
        this.tiles = new Tile[size][size];
        for(int i1 = 0; i1 < size; i1++){
            for(int i2 = 0; i2 < size; i2++){
                tiles[i1][i2] = Tile.NONE;
            }
        }
        this.size = size - 1;
        this.layerIn = layer;
    }

    public void setPlatePos(Vector3i platePos) {
        this.platePos = platePos;
    }

    public Vector3i getPlatePos() {
        return platePos;
    }

    public boolean isInsidePlate(Vector2i tilePos, int y){
        int x = tilePos.x / 100;
        int z = tilePos.y / 100;
        return platePos.x == x && platePos.y == z && this.platePos.y == y;
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

    public void fill(int y, Supplier<Tile> toFill) {
        for(int x = 0; x < size; x++){
            for(int z = 0; z < size; z++){
                setTileAt(new Vector2i(x, z), toFill.get());
            }
        }
    }
}
