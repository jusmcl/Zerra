package com.zerra.common.world.entity;

import com.zerra.common.world.World;
import com.zerra.common.world.item.Item;
import com.zerra.common.world.item.ItemGroup;

public class EntityItem extends EntityBase
{
	private World world;

	private ItemGroup group;

	public EntityItem(World world, Item item)
	{
		this(world, new ItemGroup(item, 1, 0));
	}

	public EntityItem(World world, ItemGroup group)
	{
		super(world);
	}

	public World getWorld()
	{
		return world;
	}

	public ItemGroup getGroup()
	{
		return group;
	}
}
