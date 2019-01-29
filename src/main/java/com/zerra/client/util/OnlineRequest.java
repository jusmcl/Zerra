package com.zerra.client.util;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em>
 * 
 * <br>
 * </br>
 * 
 * Allows the ability to easily send a request online to receive data.
 * 
 * @author Ocelot5836
 */
public class OnlineRequest
{
	private static final ExecutorService POOL = Executors.newCachedThreadPool();
	private static final Logger LOGGER = LogManager.getLogger("Online Request");

	/**
	 * Requests a string from the specified URL.
	 * 
	 * @param url
	 *            The url to send the request
	 * @param consumer
	 *            The callback for when the response is returned
	 */
	public static void request(String url, Consumer<String> consumer)
	{
		request(url, consumer, null);
	}

	/**
	 * Requests an input stream from the specified URL.
	 * 
	 * @param url
	 *            The url to send the request
	 * @param consumer
	 *            The callback for when the response is returned
	 */
	public static void requestStream(String url, Consumer<InputStream> consumer)
	{
		requestStream(url, consumer, null);
	}

	/**
	 * Requests a string from the specified URL.
	 * 
	 * @param url
	 *            The url to send the request
	 * @param consumer
	 *            The callback for when the response is returned
	 * @param error
	 *            The callback for if an error occurs
	 */
	public static void request(String url, Consumer<String> consumer, @Nullable Runnable error)
	{
		requestStream(url, (stream) ->
		{
			try
			{
				consumer.accept(IOUtils.toString(stream, Charset.defaultCharset()));
			}
			catch (Exception e)
			{
				LOGGER.warn("Could not convert url \'" + url + "\' to a string", e);
				if (error != null)
				{
					error.run();
				}
			}
		}, error);
	}

	/**
	 * Requests an input stream from the specified URL.
	 * 
	 * @param url
	 *            The url to send the request
	 * @param consumer
	 *            The callback for when the response is returned
	 * @param error
	 *            The callback for if an error occurs
	 */
	public static void requestStream(String url, Consumer<InputStream> consumer, @Nullable Runnable error)
	{
		if (POOL.isShutdown())
		{
			LOGGER.warn("Can not send an online request after it is disposed.");
			if (error != null)
			{
				error.run();
			}
		}
		else
		{
			POOL.execute(() ->
			{
				InputStream stream = openStream(url);
				if (stream != null)
				{
					consumer.accept(stream);
				}
				else
				{
					LOGGER.warn("Could not send a request to \'" + url + "\'. Most likely an invalid URL");
					if (error != null)
					{
						error.run();
					}
				}
			});
		}
	}

	/**
	 * Shuts down all pending requests and no longer allows requests to be sent.
	 */
	public static void shutdown()
	{
		int remainingTasks = POOL.shutdownNow().size();
		LOGGER.info("Terminated " + remainingTasks + " online requests.");
	}

	/**
	 * Opens a stream to the specified URL.
	 * 
	 * @param url
	 *            The url to open
	 * @return The stream to the URL or null if it could not be created
	 */
	@Nullable
	private static InputStream openStream(String url)
	{
		try
		{
			URLConnection connection = new URL(url).openConnection();
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
			return connection.getInputStream();
		}
		catch (Exception e)
		{
			LOGGER.fatal("Could not open stream to \'" + url + "\'", e);
			return null;
		}
	}
}