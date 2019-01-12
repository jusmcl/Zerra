package com.zerra.api.mod;

public class ModInfo
{

	private String domain;
	private String modName;
	private String modVersion;
	private String zerraVersion;
	
	private String modDescription;
	private String websiteURL;
	private String credits;

	private String[] authors = null;
	
	private String[] dependencies = null;

	public ModInfo(String domain, String modName, String modVersion, String zerraVersion)
	{
		this.domain = domain;
		this.modName = modName;
		this.modVersion = modVersion;
		this.zerraVersion = zerraVersion;
	}

	public String getDomain()
	{
		return domain;
	}

	public String getModName()
	{
		return modName;
	}

	public String getModVersion()
	{
		return modVersion;
	}

	public String getZerraVersions()
	{
		return zerraVersion;
	}

	public String[] getAuthors()
	{
		return authors;
	}

	public ModInfo setAuthors(String... authors)
	{
		this.authors = authors;
		return this;
	}

	public String[] getDependencies()
	{
		return dependencies;
	}

	public ModInfo setDependencies(String... dependencies)
	{
		this.dependencies = dependencies;
		return this;
	}

	public String getModDescription()
	{
		return modDescription;
	}

	public ModInfo setModDescription(String modDescription)
	{
		this.modDescription = modDescription;
		return this;
	}

	public String getWebsiteURL()
	{
		return websiteURL;
	}

	public ModInfo setWebsiteURL(String websiteURL)
	{
		this.websiteURL = websiteURL;
		return this;
	}

	public String getCredits()
	{
		return credits;
	}

	public ModInfo setCredits(String credits)
	{
		this.credits = credits;
		return this;
	}
}
