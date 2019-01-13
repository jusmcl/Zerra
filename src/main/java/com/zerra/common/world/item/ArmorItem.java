package com.zerra.common.world.item;

public class ArmorItem extends Item
{

	private ArmorType type;

	public ArmorItem(String registryName, ArmorType type)
	{
		super(registryName);
		this.type = type;
	}

	public ArmorType getType()
	{
		return type;
	}
}
