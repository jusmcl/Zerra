package com.zerra.common.world.entity;

import com.zerra.client.ZerraClient;
import com.zerra.common.world.World;
import com.zerra.common.world.item.Item;
import com.zerra.common.world.item.ItemGroup;

public class EntityItem extends Entity
{
	private ItemGroup group;

	public EntityItem(World world, Item item)
	{
		this(world, new ItemGroup(item, 1, 0));
	}

	public EntityItem(World world, ItemGroup group)
	{
		super(world);
	}

	public ItemGroup getGroup()
	{
		return group;
	}

	@Override
	public void update()
	{
		super.update();

		if (this.getTicksExisted() >= (ZerraClient.getInstance().getTicksPerSecond() * 60 * 5))
		{
			this.despawn();
		}
	}
}
