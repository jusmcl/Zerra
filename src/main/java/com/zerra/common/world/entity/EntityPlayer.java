package com.zerra.common.world.entity;

import com.zerra.common.world.World;

public class EntityPlayer extends Entity
{
	public EntityPlayer(World world)
	{
		super(world);
		this.setRegistryName("player");
	}
}