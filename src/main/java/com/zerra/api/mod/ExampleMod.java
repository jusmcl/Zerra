package com.zerra.api.mod;

public class ExampleMod implements Mod
{

	@Override
	public void init()
	{
		// Execute code during initialization.
	}

	@Override
	public void postInit()
	{
		// Execute code post initialization.
	}

	@Override
	public ModInfo modInfo()
	{
		return new ModInfo("examplemod", "Example Mod", "1.0", "0.0.3")
				.setAuthors("Arpaesis")
				.setDependencies("dependencyDomain")
				.setModDescription("An example mod for other modders to see")
				.setWebsiteURL("https://www.examplemodsite.com")
				.setCredits("Thanks to the Zerra development team for making Zerra!");
	}

}
