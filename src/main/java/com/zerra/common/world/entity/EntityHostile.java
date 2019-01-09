package com.zerra.common.world.entity;

import com.zerra.common.world.World;

public abstract class EntityHostile extends EntityLivingBase implements Hostile
{

	private Entity attackTarget;

	public EntityHostile(World world)
	{
		super(world);
	}

	@Override
	public Entity getAttackTarget()
	{
		return this.attackTarget;
	}

	@Override
	public void setAttackTarget(Entity target)
	{
		this.attackTarget = target;
	}
}
