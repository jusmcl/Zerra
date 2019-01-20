package com.zerra.api.mod.info;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface ModInfo
{
	
	default Logger logger()
	{
		return LogManager.getLogger(this.getDomain());
	}
	
	String getDomain();

	String getModName();

	String getModVersion();

	String getZerraVersion();

	String[] getAuthors();

	String[] getDependencies();

	String getModDescription();

	String getWebsiteURL();

	String getCredits();
}
