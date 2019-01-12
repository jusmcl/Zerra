package com.zerra.api.mod;

import com.zerra.api.mod.Mod;
import com.zerra.api.mod.info.ModInfo;
import com.zerra.api.mod.info.ModInfoBuilder;

public class ExampleMod implements Mod
{

	@Override
	public void init()
	{
		System.out.println("Initializing mod...");
	}

	@Override
	public void postInit()
	{
		System.out.println(this.getModInfo().getModName() + " has finished initialization.");
	}

	@Override
	public ModInfo getModInfo()
	{
		return new ModInfoBuilder()
				.setAuthors("Arpaesis")
				.setCredits("Credits to the Zerra development team for making the game!")
				.setDependencies("dependencyDomain")
				.setModDescription("A simple example mod.")
				.setWebsiteURL("https://www.example.com")
				.build("examplemod", "Example Mod", "1.0", "0.0.4");
	}
}

