package com.zerra.common.world.item.impl;

import com.zerra.common.world.item.tool.Tool;
import com.zerra.common.world.item.tool.ZerraToolTypes;

public abstract class BaseTool extends BaseItem implements Tool
{

	private float swingSpeed = 1.5f;
	private float reach = 3.0f;
	private int durability = 0;
	private String toolType = ZerraToolTypes.NONE;

	public BaseTool(String registryName)
	{
		super(registryName);
	}

	@Override
	public float getSwingSpeed()
	{
		return this.swingSpeed;
	}

	@Override
	public void setSwingSpeed(float swingSpeed)
	{
		this.swingSpeed = swingSpeed;
	}

	@Override
	public float getReach()
	{
		return this.reach;
	}

	@Override
	public void setReach(float reach)
	{
		this.reach = reach;
	}

	@Override
	public int getDurability()
	{
		return this.durability;
	}

	@Override
	public void setDurability(int durability)
	{
		this.durability = durability;
	}

	@Override
	public String getToolType()
	{
		return this.toolType;
	}

	@Override
	public void setToolType(String toolType)
	{
		this.toolType = toolType;
	}

}
