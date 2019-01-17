package com.zerra.api.mod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zerra.api.mod.info.ModInfo;

public interface Mod
{
	default Logger getLogger()
	{
		return LogManager.getLogger(getModInfo().getModName());
	}

	void init(ModInit modInit);

	void postInit();

	ModInfo getModInfo();
}
