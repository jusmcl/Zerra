package com.zerra.common.world.item;

import com.zerra.common.registry.RegistryNameable;
import com.zerra.common.world.World;
import com.zerra.common.world.entity.Entity;
import com.zerra.common.world.tile.Tile;

public class Item implements RegistryNameable
{
	private String registryName;

	private final ItemRarity RARITY;
	
	private boolean canBeGrouped = true;

	public Item(String registryName)
	{
		this(registryName, ItemRarity.COMMON);
	}

	public Item(String registryName, ItemRarity rarity)
	{
		this.registryName = registryName;
		this.RARITY = rarity;
		this.setCanBeGrouped(true);
	}

    @Override
    public final void setDomain(String domain) {
		this.registryName = RegistryNameable.injectDomain(this.registryName, domain);
    }

    @Override
    public String getRegistryName() {
		return registryName;
	}

	public boolean onLeftClick(World world, ItemGroup group)
	{
		return true;
	}

	public boolean onRightClick(World world, ItemGroup group)
	{
		return true;
	}

	public boolean onLeftClickEntity(Entity entity, World world, ItemGroup group)
	{
		return true;
	}

	public boolean onRightClickEntity(Entity entity, World world, ItemGroup group)
	{
		return true;
	}

	public boolean onLeftClickTile(Tile tile, World world, ItemGroup group)
	{
		return true;
	}

	public boolean oRightClickTile(Tile tile, World world, ItemGroup group)
	{
		return true;
	}

	public ItemRarity getRARITY()
	{
		return RARITY;
	}

	public boolean canBeGrouped()
	{
		return canBeGrouped;
	}

	public void setCanBeGrouped(boolean canBeGrouped)
	{
		this.canBeGrouped = canBeGrouped;
	}
}
