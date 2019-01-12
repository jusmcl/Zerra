package com.zerra.api.mod;

import com.zerra.api.mod.info.ModInfo;
import com.zerra.api.mod.info.ModInfoBuilder;

public class ExampleMod implements Mod
{

	ModInfoBuilder builder = new ModInfoBuilder();
	
	@Override
	public void init()
	{
		this.getLogger().info("Initializing mod...");
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
				.setDependencies("dependencyDomain")
				.setModDescription("A simple example mod.")
				.setWebsiteURL("https://www.example.com")
				.build("examplemod", "Example Mod", "1.0", "0.0.4");
	}
}

