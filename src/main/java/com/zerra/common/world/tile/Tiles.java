package com.zerra.common.world.tile;

import java.util.Map;

import com.google.common.collect.Maps;
import com.zerra.client.util.ResourceLocation;
import com.zerra.common.world.tile.impl.TileGround;

public class Tiles {

	private static final Map<ResourceLocation, Tile> TILES = Maps.<ResourceLocation, Tile>newHashMap();

	public static final Tile STONE = new TileGround(DefaultTileTypes.STONE, 0xa6a6a6, new ResourceLocation("textures/stone.png"), "stone");
	public static final Tile GRASS = new TileGround(DefaultTileTypes.GRASS, 0x009900, new ResourceLocation("textures/grass.png"), "grass");

	public static void registerTiles() {
		register(STONE);
		register(GRASS);
	}

	private static void register(Tile tile) {
		if (tile == null || tile.getRegistryID() == null)
			throw new RuntimeException("Tile \'" + (tile == null ? tile : tile.getClass().getName()) + "\' was either null or had no registry name!");
		TILES.put(tile.getRegistryID(), tile);
	}
	
	public static Tile[] getTiles() {
		return TILES.values().toArray(new Tile[0]);
	}
}