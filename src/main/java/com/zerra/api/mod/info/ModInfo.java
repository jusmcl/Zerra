package com.zerra.api.mod.info;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * Used to hold important information about a mod.
 *
 * @author Arpaesis
 * @author Tebreca
 */
public interface ModInfo
{

	/**
	 * Empty instance, used by {@link ModInfoBuilder} when an exception is thrown, so it doesn't have to return null
	 */
    ModInfo EMPTY = new ModInfo() {
		@Override
		public String getDomain() {
			return "";
		}
		@Override
		public String getModName() {
			return "";
		}

		@Override
		public String getModVersion() {
			return "";
		}

		@Override
		public String getZerraVersion() {
			return "";
		}
		@Override
		public String[] getAuthors() {
			return new String[0];
		}
		@Override
		public String[] getDependencies() {
			return new String[0];
		}
		@Override
		public String getModDescription() {
			return "";
		}
		@Override
		public String getWebsiteURL() {
			return "";
		}
		@Override
		public String getCredits() {
			return "";
		}
	};

    /**
	 * A logger provided automatically for mods to use, customized to use their
	 * domain name.
	 * 
	 * @return The logger for the mod.
	 */
	default Logger logger()
	{
		return LogManager.getLogger(this.getDomain());
	}

	/**
	 * @return The domain for the mod.
	 */
	String getDomain();

	/**
	 * @return The formal name of the mod.
	 */
	String getModName();

	/**
	 * @return The mod's version.
	 */
	String getModVersion();

	/**
	 * @return The game's version. This is used to determine whether or not the mod
	 *         is suitable for the running Zerra game.
	 */
	String getZerraVersion();

	/**
	 * @return The authors of the mod.
	 */
	String[] getAuthors();

	/**
	 * @return The dependencies the mod uses, if any at all. Should be specified as
	 *         domains.
	 */
	String[] getDependencies();

	/**
	 * @return The description for the mod, if it has one.
	 */
	String getModDescription();

	/**
	 * @return The mod's website URL, if it has one.
	 */
	String getWebsiteURL();

	/**
	 * @return The credits of the mod, if it has any.
	 */
	String getCredits();
}
