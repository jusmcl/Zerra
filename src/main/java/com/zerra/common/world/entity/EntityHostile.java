package com.zerra.common.world.entity;

import java.util.UUID;

import com.zerra.common.world.storage.Layer;
import com.zerra.common.world.storage.plate.WorldLayer;
import com.zerra.common.world.storage.sdf.SimpleDataFormat;

public abstract class EntityHostile extends EntityLiving implements HostileEntity
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
    public SimpleDataFormat writeToSDF() {
        SimpleDataFormat sdf = super.writeToSDF();
        sdf.setUUID("attackTarget", attackTarget.getUuid());
        return sdf;
    }

    @Override
    public void readFromSDF(SimpleDataFormat sdf) {
        super.readFromSDF(sdf);
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
