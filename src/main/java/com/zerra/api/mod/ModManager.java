package com.zerra.api.mod;

import java.util.HashMap;
import java.util.Map;

/**
 * The class where mods loaded onto the classpath will be handled and managed.
 * 
 * @author Arpaesis
 *
 */
public class ModManager
{
	
	public static Map<String, Mod> loadedMods = new HashMap<>();
	public static Map<Integer, Mod> modGroupOrder = new HashMap<>();
}
