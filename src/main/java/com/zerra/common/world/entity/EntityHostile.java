package com.zerra.common.world.entity;

import com.zerra.common.world.storage.Layer;
import com.zerra.common.world.storage.SimpleDataFormat;
import com.zerra.common.world.storage.plate.WorldLayer;

import java.util.UUID;

public abstract class EntityHostile extends EntityLivingBase implements HostileEntity
{
	private Entity attackTarget;

    public EntityHostile(WorldLayer worldLayer) {
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

    @Override
    public SimpleDataFormat serialise() {
        SimpleDataFormat sdf = super.serialise();
        sdf.setUUID("attackTarget", attackTarget.getUuid());
        return sdf;
    }

    @Override
    public void deserialise(SimpleDataFormat sdf) {
        super.deserialise(sdf);
        UUID targetUuid = sdf.getUUID("attackTarget");
        if (targetUuid == null) {
            attackTarget = null;
        } else {
            Layer layer = getLayer();
            if (layer != null) {
                attackTarget = layer.getEntityByUUID(targetUuid);
            }
        }
    }
}
