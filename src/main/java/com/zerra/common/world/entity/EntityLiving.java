package com.zerra.common.world.entity;

import com.zerra.common.event.entity.EntityDeathEvent;
import com.zerra.common.event.entity.EntityLivingEvent;
import com.zerra.common.world.World;

public class EntityLiving extends Entity
{
	public EntityLiving(World world)
	{
		super(world);
	}

	// TODO move this to the spot where entities are removed
	public void death()
	{
		EntityDeathEvent deathEvent = EntityLivingEvent.onEntityDeath(this);
		deathEvent.call();
		if (deathEvent.isCancelled())
			return;
	}
}
