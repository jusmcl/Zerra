package com.zerra.api.mod.example;

import com.zerra.api.mod.Mod;
import com.zerra.api.mod.ModInit;
import com.zerra.api.mod.info.ModInfo;
import com.zerra.api.mod.info.ModInfoBuilder;

public class ExampleDependency2 implements Mod
{

	@Override
	public void init(ModInit modInit)
	{
		this.getLogger().info(this.getModInfo().getModName() + " has finished initialization.");
	}

	@Override
	public void postInit()
	{
	}

	@Override
	public ModInfo getModInfo()
	{
		return new ModInfoBuilder("exampledependency2", "Example Dependency 2", "1.0", "0.0.6").setAuthors("Arpaesis").setCredits("Credits to the Zerra development team for making the game!")
				.setModDescription("A simple example dependency.").setWebsiteURL("https://www.example.com").setDependencies("anotherexampledependency").build();
	}
}
