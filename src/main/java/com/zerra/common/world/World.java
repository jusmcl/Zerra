package com.zerra.common.world;

import com.zerra.common.world.storage.plate.LayerPlates;

public class World {

    protected final LayerPlates[] layers = new LayerPlates[6];

    public World(){
        for(int i = 0; i < 6; i++){
            layers[i] = new LayerPlates();
        }

    }



}
