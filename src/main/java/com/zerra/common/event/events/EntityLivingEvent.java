package com.zerra.common.event.events;

import com.zerra.common.world.entity.Entity;

public class EntityLivingEvent extends EntityEvent
{

	public static EntityDeathEvent onEntityDeath(Entity entity)
	{
		return new EntityDeathEvent(entity);
	}
}
