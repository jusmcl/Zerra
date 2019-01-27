package com.zerra.api.mod.config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;

import com.zerra.common.util.JsonWrapper;
import com.zerra.common.world.storage.IOManager;

public class Configuration
{

	private File configFile;
	private JsonWrapper configWrapper;
	private String configDir = IOManager.instanceDir + "/mods/config/";

	public Configuration(String name)
	{
		new File(configDir).mkdirs();

		this.configFile = new File(configDir, name + ".json");

		try
		{
			if (!configFile.exists())
			{

				configFile.createNewFile();
				FileUtils.writeStringToFile(configFile, "{}", StandardCharsets.UTF_8.name());

			} else if (FileUtils.readFileToString(configFile, StandardCharsets.UTF_8.name()).isEmpty())
			{
				FileUtils.writeStringToFile(configFile, "{}", StandardCharsets.UTF_8.name());
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		configWrapper = new JsonWrapper(configFile);
		this.configWrapper.close();
	}

	public JsonWrapper getConfig()
	{
		return configWrapper;
	}
}
