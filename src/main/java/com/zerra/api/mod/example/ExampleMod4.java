package com.zerra.api.mod.example;

import com.zerra.api.mod.Mod;
import com.zerra.api.mod.ModInit;
import com.zerra.api.mod.info.ModInfo;
import com.zerra.api.mod.info.ModInfoBuilder;
import com.zerra.common.world.item.Item;

public class ExampleMod4 implements Mod
{

	ModInfoBuilder builder = new ModInfoBuilder("examplemod4", "Example Mod 4", "1.0", "0.0.4");
	
	@Override
	public void init(ModInit modInit) {
		Item item = new Item("testItem");
		modInit.register(item);

		float progress = 0;
		for(int i = 0; i < 100000; i++)
		{
			progress = (float)i / 1000;
			System.out.println("MOD 4 PROGRESS: " + progress + "%");
		}
		
		this.getLogger().info(this.getModInfo().getModName() + " has finished initialization.");
	}

	@Override
	public void postInit()
	{
	}

	@Override
	public ModInfo getModInfo()
	{
		return builder
				.setAuthors("Arpaesis")
				.setCredits("Credits to the Zerra development team for making the game!")
				.setDependencies("exampledependency", "exampledependency2", "anotherexampledependency")
				.setModDescription("A simple example mod.")
				.setWebsiteURL("https://www.example.com")
				.build();
	}
}

