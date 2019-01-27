package com.zerra.api.mod.example;

import com.zerra.api.mod.config.Conf;
import com.zerra.api.mod.config.Conf.Doc;

@Conf
public class ExampleConfiguration
{
	@Doc("Determine whether or not entities from this mod should load.")
	public static boolean entitiesShouldLoad = false;
}
