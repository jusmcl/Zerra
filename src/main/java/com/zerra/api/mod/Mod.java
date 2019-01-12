package com.zerra.api.mod;

import com.zerra.api.mod.info.ModInfo;

public interface Mod
{

	public void init();

	public void postInit();

	public ModInfo getModInfo();
}
