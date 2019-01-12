package com.zerra.api.mod.info;

public class ModInfoBuilder
{
	String domain;

	String modName;

	String modVersion;

	String zerraVersion;

	String modDescription;

	String websiteURL;

	String credits;

	String[] authors, dependencies;

	boolean isInitialized = false;

	public ModInfoBuilder setModDescription(String modDescription)
	{
		this.modDescription = modDescription;
		return this;
	}

	public ModInfoBuilder setWebsiteURL(String websiteURL)
	{
		this.websiteURL = websiteURL;
		return this;
	}

	public ModInfoBuilder setCredits(String credits)
	{
		this.credits = credits;
		return this;
	}

	public ModInfoBuilder setAuthors(String... authors)
	{
		this.authors = authors;
		return this;
	}

	public ModInfoBuilder setDependencies(String... dependencies)
	{
		this.dependencies = dependencies;
		return this;
	}

	public ModInfo build(String domain, String modName, String modVersion, String zerraVersion)
	{
		this.domain = domain;
		this.modName = modName;
		this.modVersion = modVersion;
		this.zerraVersion = zerraVersion;

		return new ModInfo(this);
	}
}
