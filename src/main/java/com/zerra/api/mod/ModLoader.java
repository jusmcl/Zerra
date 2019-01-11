package com.zerra.api.mod;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

public class ModLoader
{

	public List<Mod> loadMods(String directory)
	{
		List<Mod> mods = new ArrayList<>();

		File modDir = new File(directory);
		for (File jar : modDir.listFiles())
		{

			try
			{
				System.out.println("Attempting to load " + jar.getName());

				ClassLoader mainLoader = ModLoader.class.getClassLoader();

				URLClassLoader modLoader = new URLClassLoader(new URL[]
				{ jar.toURI().toURL() }, mainLoader);

				Reflections reflections = new Reflections(modLoader, new SubTypesScanner());

				Set<Class<? extends Mod>> subTypes = reflections.getSubTypesOf(Mod.class);

				List<Class<? extends Mod>> mainList = new ArrayList<>(subTypes);

				Class<?> clazz = mainList.get(0);

				Class<? extends Mod> newClass = clazz.asSubclass(Mod.class);

				Constructor<? extends Mod> constructor = newClass.getConstructor();

				mods.add(constructor.newInstance());
			} catch (Exception e)
			{
				e.printStackTrace();
			}

		}

		return mods;
	}
}