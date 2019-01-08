package com.zerra.common.world.item;

import com.zerra.common.world.World;
import com.zerra.common.world.entity.Entity;

public class Item
{
	public Item(String registryName)
	{

	}

	public boolean onLeftClickEntity(Entity entity, World world)
	{
		return true;
	}

	public boolean onRightClickEntity(Entity entity, World world)
	{
		return true;
	}
}
