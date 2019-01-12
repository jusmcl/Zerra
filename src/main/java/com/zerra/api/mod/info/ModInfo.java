package com.zerra.api.mod.info;

public class ModInfo
{

	ModInfoBuilder builder;

	public ModInfo(ModInfoBuilder builder)
	{
		this.builder = builder;
	}

	public String getDomain()
	{
		return builder.domain;
	}

	public String getModName()
	{
		return builder.modName;
	}

	public String getModVersion()
	{
		return builder.modVersion;
	}

	public String getZerraVersion()
	{
		return builder.zerraVersion;
	}

	public String[] getAuthors()
	{
		return builder.authors;
	}

	public String[] getDependencies()
	{
		return builder.dependencies;
	}

	public String getModDescription()
	{
		return builder.modDescription;
	}

	public String getWebsiteURL()
	{
		return builder.websiteURL;
	}

	public String getCredits()
	{
		return builder.credits;
	}
}
