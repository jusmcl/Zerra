package com.zerra.common.world.entity;

import java.util.UUID;

import javax.annotation.Nonnull;

import com.zerra.common.util.UBObjectWrapper;
import com.zerra.common.world.World;
import com.zerra.common.world.storage.Layer;

public abstract class EntityHostile extends EntityLiving implements HostileEntity
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

	@Nonnull
	@Override
	public UBObjectWrapper writeToUBO(@Nonnull UBObjectWrapper ubo)
	{
		ubo.setUUID("attackTarget", attackTarget.getUUID());
		return super.writeToUBO(ubo);
	}

	@Override
	public void readFromUBO(@Nonnull UBObjectWrapper ubo)
	{
		super.readFromUBO(ubo);
		UUID targetUUID = ubo.getUUID("attackTarget");
		if (targetUUID == null)
		{
			attackTarget = null;
		} else
		{
			Layer layer = getLayer();
			if (layer != null)
			{
				attackTarget = layer.getEntityByUUID(targetUUID);
			}
		}
	}
}
