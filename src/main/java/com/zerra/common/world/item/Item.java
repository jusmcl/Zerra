package com.zerra.common.world.item;

import com.zerra.common.world.World;
import com.zerra.common.world.entity.Entity;
import com.zerra.common.world.tile.Tile;

public class Item
{
	private String registryName;

	public Item(String registryName)
	{
		this.registryName = registryName;
	}

	public String getRegistryName()
	{
		return registryName;
	}

	public boolean onLeftClick(World world, ItemGroup group)
	{
		return true;
	}

	public boolean onRightClick(World world, ItemGroup group)
	{
		return true;
	}

	public boolean onLeftClickEntity(Entity entity, World world, ItemGroup group)
	{
		return true;
	}

	public boolean onRightClickEntity(Entity entity, World world, ItemGroup group)
	{
		return true;
	}

	public boolean onLeftClickTile(Tile tile, World world, ItemGroup group)
	{
		return true;
	}

	public boolean oRightClickTile(Tile tile, World world, ItemGroup group)
	{
		return true;
	}
}
