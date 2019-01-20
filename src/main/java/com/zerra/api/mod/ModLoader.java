package com.zerra.api.mod;

import com.zerra.client.Zerra;
import com.zerra.common.util.MiscUtils;
import org.reflections.Reflections;

import java.util.Set;

public class ModLoader {
	private ModManager modManager;

	public ModLoader(ModManager modManager) {
		this.modManager = modManager;
	}

	public void loadMods(String directory) {
		Zerra.logger().info("Started loading mods...");

		long snapshot = System.currentTimeMillis();

		Reflections reflections = new Reflections();

		Set<Class<? extends Mod>> loadedMods = reflections.getSubTypesOf(Mod.class);

		for (Class<? extends Mod> clazz : loadedMods) {
			Mod mod = null;
			try {
				mod = clazz.getConstructor().newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (mod != null) {
				modManager.loadedMods.put(mod.getModInfo().getDomain(), mod);
			}
		}

		Zerra.logger().info("Finished loading mods in " + MiscUtils.secondsSinceTime(snapshot));
	}
}