package com.zerra.api.mod;

import java.util.List;

import com.zerra.common.ZerraClient;
import com.zerra.common.util.MiscUtils;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

public class ModLoader
{
	private ModManager modManager;

	public ModLoader(ModManager modManager)
	{
		this.modManager = modManager;
	}

	public void loadMods(String directory)
	{
		long snapshot = System.currentTimeMillis();

		String modInterface = "com.zerra.api.mod.Mod";

		try (ScanResult scanResult = new ClassGraph().enableAllInfo().scan())
		{
			List<Class<?>> widgetClasses = scanResult.getClassesImplementing(modInterface).loadClasses();

			for (Class<?> modClass : widgetClasses)
			{
				Mod mod = (Mod) modClass.getConstructor().newInstance();

				if (mod != null)
				{
					System.out.println(mod.getModInfo().getDomain());
					modManager.loadedMods.put(mod.getModInfo().getDomain(), mod);
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		ZerraClient.logger().info("Finished loading mods in " + MiscUtils.secondsSinceTime(snapshot));

	}
}