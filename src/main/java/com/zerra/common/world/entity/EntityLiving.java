package com.zerra.common.world.entity;

import com.zerra.common.event.entity.EntityDeathEvent;
import com.zerra.common.event.entity.EntityLivingEvent;
import com.zerra.common.world.storage.plate.WorldLayer;

public class EntityLiving extends Entity
{

	public EntityLiving(WorldLayer worldLayer)
	{
		super(worldLayer);
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
