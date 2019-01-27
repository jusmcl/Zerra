package com.zerra.api.mod.info;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.zerra.api.mod.config.Conf;
import com.zerra.api.mod.config.Configuration;
import com.zerra.client.util.Maths;
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

	private Class configClass;

	public ModInfoBuilder(String domain, String modName, String modVersion, String zerraVersion)
	{
		this.domain = domain;
		this.modName = modName;
		this.modVersion = modVersion;
		this.zerraVersion = zerraVersion;
	}

	/**
	 * Creates a modinfo instance from a JSON file.
	 * 
	 * @param fileInputStream The stream for the file.
	 * @return A new ModInfo object.
	 */
	@Nullable
	public static ModInfo fromJSONFile(String path)
	{
		FileInputStream fileInputStream = null;
		try
		{
			fileInputStream = new FileInputStream(path);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
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

	public ModInfoBuilder setConfigClass(Class<?> configClass)
	{
		try
		{
			if (configClass.isAnnotationPresent(Conf.class))
			{
				this.configClass = configClass;

				Configuration conf = new Configuration(this.domain);

				for (Field field : configClass.getFields())
				{

					boolean flag = field.getDeclaredAnnotation(Conf.Range.class) != null;

					int min = 0;
					int max = 0;
					if (flag)
					{
						min = field.getDeclaredAnnotation(Conf.Range.class).min();
						max = field.getDeclaredAnnotation(Conf.Range.class).max();
					}

					if (field.getDeclaredAnnotation(Conf.Doc.class) != null)
					{

						String rangeAddon = "";
						if (flag)
						{
							rangeAddon = " The minimum value is " + min + " and the maximum value is " + max;
						}
						conf.getConfig().putSafe("#doc-" + field.getName(), field.getDeclaredAnnotation(Conf.Doc.class).value() + rangeAddon);

						if (field.getType().isAssignableFrom(int.class))
						{
							conf.getConfig().putSafe(field.getName(), Maths.clamp(field.getInt(configClass), min, max));
						} else if (field.getType().isAssignableFrom(double.class))
						{
							conf.getConfig().putSafe(field.getName(), flag ? Maths.clampD(field.getDouble(configClass), (double) min, (double) max) : field.getDouble(configClass));
						} else if (field.getType().isAssignableFrom(float.class))
						{
							conf.getConfig().putSafe(field.getName(), flag ? Maths.clampF(field.getFloat(configClass), (float) min, (float) max) : field.getFloat(configClass));
						} else if (field.getType().isAssignableFrom(long.class))
						{
							conf.getConfig().putSafe(field.getName(), field.getLong(configClass));
						} else if (field.getType().isAssignableFrom(short.class))
						{
							conf.getConfig().putSafe(field.getName(), field.getShort(configClass));
						} else if (field.getType().isAssignableFrom(byte.class))
						{
							conf.getConfig().putSafe(field.getName(), field.getByte(configClass));
						} else if (field.getType().isAssignableFrom(char.class))
						{
							conf.getConfig().putSafe(field.getName(), field.getChar(configClass));
						} else if (field.getType().isAssignableFrom(String.class))
						{
							conf.getConfig().putSafe(field.getName(), (String) field.get(configClass));
						} else if (field.getType().isAssignableFrom(boolean.class))
						{
							conf.getConfig().putSafe(field.getName(), field.getBoolean(configClass));
						} else
						{
							// TODO: Maybe handle objects?
						}
					}
				}
			} else
			{
				throw new RuntimeException(String.format("Class with domain %s tried to set a config without the %s annotation!", this.domain, Conf.class.getName()));
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
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
