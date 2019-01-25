package com.zerra.client.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.google.common.collect.Maps;
import com.zerra.client.ZerraClient;
import com.zerra.common.Reference;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * Allows the ability to localize string by calling
 * {@link I18n#format(String, Object...)}.
 * 
 * @author Ocelot5836
 */
public class I18n
{

	private static List<ResourceBundle> bundles = new ArrayList<ResourceBundle>();
	private static Map<String, String> translated = Maps.<String, String>newHashMap();

	/**
	 * Gets the specified key from the language file selected.
	 * 
	 * @param key The key of the string
	 * @param params The same as used in {@link String#format(String, Object...)}
	 * @return Formats a string to the proper language
	 */
	public static String format(String key, Object... params)
	{
		for (ResourceBundle bundle : bundles)
		{
			try
			{
				if (translated.containsKey(key))
				{
					return String.format(translated.get(key), params);
				}

				String formatted = bundle.getString(key);
				if (!formatted.equals(key))
				{
					translated.put(key, formatted);
					return String.format(formatted, params);
				}
			} catch (Exception e)
			{
			}
		}

		return key;
	}

	/**
	 * Checks to see if the specified key has a translation available.
	 * 
	 * @param key The key of the string in the language file
	 * @return Whether or not there is a translation for the key provided
	 */
	public static boolean contains(String key)
	{
		if (translated.containsKey(key))
			return true;

		for (ResourceBundle bundle : bundles)
		{
			if (key != bundle.getString(key))
			{
				translated.put(key, bundle.getString(key));
				return true;
			}
		}
		return false;
	}

	/**
	 * Sets the language to the specified Locale. Will default to english if there
	 * was no language file found for that locale
	 * 
	 * @param locale The new language to get the languages from.
	 */
	public static void setLanguage(Locale locale)
	{
		Locale english = new Locale("en", "us");
		// for (IMod mod : ModLoader.getModsList()) {
		// ResourceBundle bundle;
		// try {
		// bundle = ResourceBundle.getBundle("assets/" + mod.getModName() + "/lang/" +
		// (locale.getLanguage() + "_" + locale.getCountry()).toLowerCase(), locale);
		// } catch (Exception e) {
		// bundle = ResourceBundle.getBundle("assets/" + mod.getModName() + "/lang/" +
		// (english.getLanguage() + "_" + english.getCountry()).toLowerCase(),
		// Locale.ENGLISH);
		// }
		// bundles.add(bundle);
		// }

		ResourceBundle bundle = null;
		try
		{
			bundle = ResourceBundle.getBundle("assets/" + Reference.DOMAIN + "/lang/" + (locale.getLanguage() + "_" + locale.getCountry()).toLowerCase(), locale);
		} catch (Exception e)
		{
			ZerraClient.logger()
					.warn("Could not locate language file for \'" + Reference.DOMAIN + "\' locale \'" + locale.getLanguage() + "_" + locale.getCountry() + "\'. Defaulting to english language file.");
			try
			{
				bundle = ResourceBundle.getBundle("assets/" + Reference.DOMAIN + "/lang/" + (english.getLanguage() + "_" + english.getCountry()).toLowerCase(), english);
			} catch (MissingResourceException e1)
			{
				ZerraClient.logger().warn("Could not locate language file for \'" + Reference.DOMAIN + "\' locale \'" + english.getLanguage() + "_" + english.getCountry() + "\'");
			}
		}

		if (bundle != null)
		{
			bundles.add(bundle);
		}

		translated.clear();
	}
}