package com.zerra.api.mod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zerra.api.mod.info.ModInfo;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * The mod interface for modders to implement. This interface is how the game discovers and loads a mod.
 * 
 * @author Arpaesis
 */
public interface Mod
{
	/**
	 * Provides a custom logger by default for mods, so they don't have to create one.
	 * 
	 * @return The logger for the mod.
	 */
	default Logger getLogger()
	{
		return LogManager.getLogger(getModInfo().getModName());
	}

	/**
	 * The initialization phase of the mod. Registration goes in this method.
	 * @param modInit
	 */
	void init(ModInit modInit);

	/*
	 * Post initialization for mods. If code needs to be executed after initialization, place it in this method.
	 */
	void postInit();

	/**
	 * The mod info for the mod. Provides important details such as the Zerra version this mod best works with, dependencies the mod may have, and so on.
	 * 
	 * @return The mod info object for the mod.
	 */
	ModInfo getModInfo();
}
