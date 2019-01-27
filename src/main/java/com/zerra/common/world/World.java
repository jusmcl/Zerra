package com.zerra.common.world;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2i;

import com.zerra.common.world.entity.Entity;
import com.zerra.common.world.storage.Layer;
import com.zerra.common.world.storage.plate.WorldLayer;

public class World
{
	private boolean server;
	private Logger logger;
	private String name;
	private Random random;
	private Vector2i worldSpawnPoint;

	protected ExecutorService pool;
	protected Layer[] layers;

	private long seed;

	public World(String name, Long seed, boolean server)
	{
		this.server = server;
		this.logger = LogManager.getLogger("World-" + name + "/" + (server ? "Server" : "Client"));
		this.name = name;
		this.random = new Random();

		if (seed == null)
		{
			this.seed = random.nextLong();
			this.random.setSeed(this.seed);
		}
		else
		{
			this.random.setSeed(seed);
			this.seed = seed;
		}
		this.worldSpawnPoint = new Vector2i(random.nextInt(1024) - 512, random.nextInt(1024) - 512);

		this.pool = Executors.newCachedThreadPool();
		this.layers = new Layer[6];
		for (int i = 0; i < this.layers.length; i++)
		{
			this.layers[i] = new WorldLayer(this, i);
		}
	}

	public World(String name, boolean server)
	{
		this(name, null, server);
	}

	public void schedule(Runnable command)
	{
		this.pool.execute(command);
	}

	public void update()
	{
	}

	/**
	 * Shuts down the thread pool and saves all data
	 */
	public void stop()
	{
		this.pool.shutdown();
	}

	/**
	 * Adds the specified entity to the world.
	 * 
	 * @param entity
	 *            The entity to add
	 */
	public void addEntity(Entity entity)
	{
		Layer worldLayer = this.getLayer(entity.getLayerId());
		if (worldLayer != null)
		{
			worldLayer.getEntities().add(entity);
		}
	}

	@Nullable
	public Entity getEntityByUUID(UUID uuid)
	{
		for (Layer layer : getLayers())
		{
			Entity entity = layer.getEntityByUUID(uuid);
			if (entity != null)
			{
				return entity;
			}
		}
		return null;
	}

	public boolean isServer()
	{
		return server;
	}

	public Logger logger()
	{
		return logger;
	}

	public String getName()
	{
		return name;
	}

	public Random getRandom()
	{
		return random;
	}

	public Layer[] getLayers()
	{
		return layers;
	}

	public Vector2i getWorldSpawnPoint()
	{
		return this.worldSpawnPoint;
	}

	public Layer getLayer(int layer)
	{
		if (layer < 0 || layer >= this.layers.length)
			return null;
		return this.layers[layer];
	}

	public long getSeed()
	{
		return this.seed;
	}
}