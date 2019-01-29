package com.zerra.common.world.entity;

import java.util.List;

import com.zerra.common.world.World;
import com.zerra.common.world.entity.attrib.Attribute;
import com.zerra.common.world.entity.attrib.AttributeFactory;

public class EntityPlayer extends Entity
{
	public static final Attribute<Float> SPEED = AttributeFactory.createNumberAttribute("speed", 1.0f);

	public EntityPlayer(World world)
	{
		super(world);
		this.setRegistryName("player");
	}
	
	@Override
	protected void registerEntityAttributes(List<Attribute<?>> attributes)
	{
		attributes.add(SPEED);
	}
}