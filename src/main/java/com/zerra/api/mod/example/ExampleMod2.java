package com.zerra.api.mod.example;

import com.zerra.api.mod.Mod;
import com.zerra.api.mod.ModInit;
import com.zerra.api.mod.info.ModInfo;
import com.zerra.api.mod.info.ModInfoBuilder;
import com.zerra.common.world.item.Item;

public class ExampleMod2 implements Mod
{
	@Override
	public void init(ModInit modInit)
	{
		Item item = new Item("testItem");
		modInit.register(item);

		this.getLogger().info(this.getModInfo().getModName() + " has finished initialization.");
	}

	@Override
	public void postInit()
	{
	}

	@Override
	public ModInfo getModInfo()
	{
		return new ModInfoBuilder("examplemod2", "Example Mod 2", "1.0", "0.0.4").setAuthors("Arpaesis").setCredits("Credits to the Zerra development team for making the game!")
				.setDependencies("exampledependency", "exampledependency2", "anotherexampledependency").setModDescription("A simple example mod.").setWebsiteURL("https://www.example.com").build();
	}
}
