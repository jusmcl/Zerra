package com.zerra.common.world.tile;

import com.zerra.common.world.item.DefaultToolTypes;

public class DefaultTileTypes {

    public static final TileType GRASS = new TileType("type_grass", 1f, 1f, DefaultToolTypes.SHOVEL);

    public static final TileType STONE = new TileType("type_stone", 2f, 2f, DefaultToolTypes.SHOVEL);

    public static final TileType AIR = new TileType("type_air", 0f, 0f, DefaultToolTypes.NONE);
}
