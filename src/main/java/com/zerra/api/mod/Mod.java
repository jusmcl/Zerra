package com.zerra.api.mod;

import com.zerra.api.mod.info.ModInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface Mod
{
	default Logger getLogger()
	{
		return LogManager.getLogger(getModInfo().getModName());
	}

	void init();

	void postInit();

	ModInfo getModInfo();
}
