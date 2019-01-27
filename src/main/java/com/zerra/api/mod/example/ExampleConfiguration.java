package com.zerra.api.mod.example;

import com.zerra.api.mod.config.Conf;
import com.zerra.api.mod.config.Conf.Doc;
import com.zerra.api.mod.config.Conf.Range;

@Conf
public class ExampleConfiguration
{
	@Doc("Determine whether or not entities from this mod should load.")
	public static boolean entitiesShouldLoad = false;

	@Doc("The distance at which entities should render.")
	public static double renderDistance = 16.0D;

	@Doc("The default damage the entities can do.")
	public static float attackDamage = 1.0F;

	@Doc("The default move speed for entities.")
	@Range(min = 1, max = 10)
	public static int moveSpeed = 1;
}
