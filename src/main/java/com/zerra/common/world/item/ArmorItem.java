package com.zerra.common.world.item;

public class ArmorItem extends Item
{

	private ArmorType type;

	public ArmorItem(String registryName, ArmorType type)
	{
		super(registryName);
		this.type = type;
		this.setCanBeGrouped(false);
	}

	public ArmorType getType()
	{
		return type;
	}
}
