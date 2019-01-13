package com.zerra.common.world.entity;

import com.zerra.common.event.entity.EntityDeathEvent;
import com.zerra.common.event.entity.EntityLivingEvent;
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

	public void death()
	{
		EntityDeathEvent deathEvent = EntityLivingEvent.onEntityDeath(this);
		deathEvent.call();
		if (deathEvent.isCancelled())
			return;
	}
}
