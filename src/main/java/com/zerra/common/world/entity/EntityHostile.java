package com.zerra.common.world.entity;

import java.util.UUID;

import javax.annotation.Nonnull;

import com.zerra.common.util.UBObjectWrapper;
import com.zerra.common.world.storage.Layer;
import com.zerra.common.world.storage.plate.WorldLayer;

public abstract class EntityHostile extends EntityLiving implements HostileEntity
{
	private Entity attackTarget;

	public EntityHostile(WorldLayer worldLayer)
	{
		super(worldLayer);
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
		ubo.setUUID("attackTarget", attackTarget.getUuid());
		return super.writeToUBO(ubo);
	}

	@Override
	public void readFromUBO(@Nonnull UBObjectWrapper ubo)
	{
		super.readFromUBO(ubo);
		UUID targetUuid = ubo.getUUID("attackTarget");
		if (targetUuid == null)
		{
			attackTarget = null;
		} else
		{
			Layer layer = getLayer();
			if (layer != null)
			{
				attackTarget = layer.getEntityByUUID(targetUuid);
			}
		}
	}
}
