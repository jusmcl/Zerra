package com.zerra.common.world.entity;

import com.zerra.common.world.World;
import com.zerra.common.world.entity.attrib.Attribute;
import com.zerra.common.world.entity.attrib.AttributeFactory;

public class EntityPlayer extends Entity
{
	public static final Attribute<Double> SPEED = AttributeFactory.createNumberAttribute("speed", 1.0);

	public EntityPlayer(World world)
	{
		super(world);
		this.setRegistryName("player");
	}
}