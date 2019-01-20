package com.zerra.api.mod.info;

import java.io.FileInputStream;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.zerra.common.util.JsonWrapper;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * A builder class used by mods to construct a ModInfo object.
 *
 * @author Tebreca
 */
public class ModInfoBuilder
{

	public static String[] optionalJsonKeys =
	{ "modDescription", "websiteURL", "credits", "authors", "dependencies" };

	public static String[] requiredJsonKeys =
	{ "domain", "modname", "modVersion", "zerraVersion" };

	private String domain;

	private String modName;

	private String modVersion;

	private String zerraVersion;

	private String modDescription;

	private String websiteURL;

	private String credits;

	private String[] authors;

	private String[] dependencies;

	public ModInfoBuilder(String domain, String modName, String modVersion, String zerraVersion)
	{
		this.domain = domain;
		this.modName = modName;
		this.modVersion = modVersion;
		this.zerraVersion = zerraVersion;
	}

	/**
	 * Fetches a modinfo instance from a JSON file.
	 * 
	 * @param fileInputStream The stream for the file.
	 * @return A new ModInfo object.
	 */
	@Nullable
	public static ModInfo fromJSONFile(FileInputStream fileInputStream)
	{
		JsonWrapper jsonWrapper = new JsonWrapper(fileInputStream);
		JsonObject object = jsonWrapper.getJson();
		boolean flag = true;
		for (String key : requiredJsonKeys)
		{
			flag &= object.has(key);
		}
		if (!flag)
		{
			jsonWrapper.close();
			return null;
		}

		String domain = jsonWrapper.getString("domain");
		String modName = jsonWrapper.getString("modName");
		String modVersion = jsonWrapper.getString("modVersion");
		String zerraVersion = jsonWrapper.getString("zerraVersion");

		ModInfoBuilder builder = new ModInfoBuilder(domain, modName, modVersion, zerraVersion);

		for (String key : optionalJsonKeys)
		{
			if (object.has(key))
			{
				builder.set(key, (String[]) jsonWrapper.get(key));
			}
		}
		jsonWrapper.close();
		return builder.build();
	}

	/**
	 * A generic setter for any of the optional mod properties.
	 * 
	 * @param name The key for the property.
	 * @param value The value the property should have.
	 * @return A ModInfoBuilder with the newly set properties.
	 */
	public ModInfoBuilder set(String name, String... value)
	{
		if (value.length < 1)
		{
			return this;
		}

		String joined = String.join(" ", value);

		switch (name)
		{
		case "modDescription":
			return setModDescription(joined);
		case "websiteURL":
			return setWebsiteURL(joined);
		case "credits":
			return setCredits(joined);
		case "authors":
			return setAuthors(value);
		case "dependencies":
			return setDependencies(value);
		default:
			return this;
		}

	}

	/**
	 * @param modDescription The description to give the mod.
	 * @return A ModInfoBuilder with a set description.
	 */
	public ModInfoBuilder setModDescription(String modDescription)
	{
		this.modDescription = modDescription;
		return this;
	}

	/**
	 * @param websiteURL The website link to give the mod.
	 * @return A ModInfoBuilder with a set website URL.
	 */
	public ModInfoBuilder setWebsiteURL(String websiteURL)
	{
		this.websiteURL = websiteURL;
		return this;
	}

	/**
	 * @param credits The credits the mod shall give to others.
	 * @return A ModInfoBuilder with set credits.
	 */
	public ModInfoBuilder setCredits(String credits)
	{
		this.credits = credits;
		return this;
	}

	/**
	 * @param authors The authors of the mod.
	 * @return A ModInfoBuilder with a list of authors.
	 */
	public ModInfoBuilder setAuthors(String... authors)
	{
		this.authors = authors;
		return this;
	}

	/**
	 * @param dependencies The dependencies the mod relies on. Dependencies should
	 *        be specified as a domain.
	 * @return A ModInfoBuilder with the set dependencies.
	 */
	public ModInfoBuilder setDependencies(String... dependencies)
	{
		this.dependencies = dependencies;
		return this;
	}

	/**
	 * @return A new ModInfo object.
	 */
	public ModInfo build()
	{

		return new ModInfo()
		{
			@Override
			public String getDomain()
			{
				return domain;
			}

			@Override
			public String getModName()
			{
				return modName;
			}

			@Override
			public String getModVersion()
			{
				return modVersion;
			}

			@Override
			public String getZerraVersion()
			{
				return zerraVersion;
			}

			@Override
			public String[] getAuthors()
			{
				return authors;
			}

			@Override
			public String[] getDependencies()
			{
				return dependencies;
			}

			@Override
			public String getModDescription()
			{
				return modDescription;
			}

			@Override
			public String getWebsiteURL()
			{
				return websiteURL;
			}

			@Override
			public String getCredits()
			{
				return credits;
			}
		};
	}
}
