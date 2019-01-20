package com.zerra.common.world.tile.impl;

import com.zerra.client.util.ResourceLocation;
import com.zerra.common.world.storage.Layer;
import com.zerra.common.world.tile.Tile;
import com.zerra.common.world.tile.TileType;

public class TileGround extends Tile
{

	private ResourceLocation name;
	private int color;
	private ResourceLocation texture;

	public TileGround(TileType type, int color, ResourceLocation texture, String name)
	{
		super(type);
		this.color = color;
		this.texture = texture;
		this.name = new ResourceLocation("tile_ground_" + name);
	}

	@Override
	public ResourceLocation getRegistryID()
	{
		return name;
	}

	@Override
	public int getColor()
	{
		return color;
	}

	@Override
	public void spawnDropsInLayer(Layer layer)
	{

	}

	@Override
	public ResourceLocation getTexture()
	{
		return texture;
	}
}
