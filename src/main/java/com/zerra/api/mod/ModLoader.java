package com.zerra.api.mod;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import com.google.common.collect.Iterables;

public class ModLoader
{

	private ModManager modManager;
	
	public ModLoader(ModManager modManager)
	{
		this.modManager = modManager;
	}
	
	public void loadMods(String directory)
	{
		File modDir = new File(directory);
		for (File jar : modDir.listFiles())
		{

			try
			{
				System.out.println("Attempting to load " + jar.getName());

				Reflections reflections = new Reflections(new URLClassLoader(new URL[]
				{ jar.toURI().toURL() }, ModLoader.class.getClassLoader()), new SubTypesScanner());

				Class<? extends Mod> mod = Iterables.getFirst(reflections.getSubTypesOf(Mod.class), null);

				if (mod != null)
				{
					Mod instance = mod.getConstructor().newInstance();

					modManager.loadedMods.put(instance.getModInfo().getDomain(), instance);
				} else
				{
					System.out.println("Failed to load " + jar.getName() + " , mod does not have a valid class implementing Mod.class. The mod will not be loaded.");
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}

		}
	}
}