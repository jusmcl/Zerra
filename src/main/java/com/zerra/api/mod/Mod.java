package com.zerra.api.mod;

public interface Mod
{

	public void init();

	public void postInit();

	public ModInfo modInfo();
}
