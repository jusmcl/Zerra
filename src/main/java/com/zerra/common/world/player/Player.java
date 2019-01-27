package com.zerra.common.world.player;

import java.util.Map;

import com.zerra.common.world.World;
import com.zerra.common.world.entity.EntityLiving;
import com.zerra.common.world.entity.attrib.Attribute;
import com.zerra.common.world.entity.attrib.AttributeFactory;

public class Player extends EntityLiving
{
	public Player(World world)
	{
		super(world);
	}
	
	@Override
	protected void setEntityAttributes(Map<String, Attribute<?>> attributes)
	{
		attributes.put("test", AttributeFactory.createNumberRangeAttribute(0.0, 0.0, 20.0, 10.0));
	}
}