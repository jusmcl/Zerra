package com.zerra.common.world.entity;

import com.zerra.common.world.World;
import com.zerra.common.world.entity.attrib.SharedLivingAttributes;

public class EntityLivingBase extends EntityBase
{
	private World world;

	private SharedLivingAttributes attributes;

	public EntityLivingBase(World world)
	{
		super(world);

		this.attributes = new SharedLivingAttributes();
	}

	public World getWorld()
	{
		return world;
	}

	public SharedLivingAttributes getSharedAttributes()
	{
		return attributes;
	}
}
