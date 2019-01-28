package com.zerra.common;

public class Reference
{
	public static final String NAME = "Zerra";
	public static final String VERSION = "0.0.6";
	public static final String DOMAIN = "zerra";
	public static final boolean IS_DEVELOPMENT_BUILD = true;

	public static String prefixWithDomain(String name)
	{
		return DOMAIN + ":" + name;
	}
}
