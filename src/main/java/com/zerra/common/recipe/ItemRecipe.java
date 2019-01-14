package com.zerra.common.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.zerra.common.world.item.ItemGroup;

public class ItemRecipe
{

	private ItemGroup resultant;
	private List<ItemGroup> components = new ArrayList<>();

	public ItemRecipe(ItemGroup resultant, ItemGroup... components)
	{
		this.components = Arrays.asList(components);
	}

	public ItemGroup getResultant()
	{
		return resultant;
	}

	public List<ItemGroup> getComponents()
	{
		return components;
	}
}
