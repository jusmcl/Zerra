package com.zerra.common.world.item.impl;

import com.zerra.common.world.item.Item;
import com.zerra.common.world.item.tool.Tool;
import com.zerra.common.world.item.tool.ZerraToolTypes;

public abstract class ItemTool extends Item implements Tool
{

	private float swingSpeed = 1.5f;
	private float reach = 3.0f;
	private int durability = 0;
	private String toolType = ZerraToolTypes.NONE;

	public ItemTool(String registryName)
	{
		super(registryName);
		this.setCanBeGrouped(false);
	}

	@Override
	public float getSwingSpeed()
	{
		return this.swingSpeed;
	}

	@Override
	public Tool setSwingSpeed(float swingSpeed)
	{
		this.swingSpeed = swingSpeed;
		return this;
	}

	@Override
	public float getReach()
	{
		return this.reach;
	}

	@Override
	public Tool setReach(float reach)
	{
		this.reach = reach;
		return this;
	}

	@Override
	public int getDurability()
	{
		return this.durability;
	}

	@Override
	public Tool setDurability(int durability)
	{
		this.durability = durability;
		return this;
	}

	@Override
	public String getToolType()
	{
		return this.toolType;
	}

	@Override
	public Tool setToolType(String toolType)
	{
		this.toolType = toolType;
		return this;
	}

}
