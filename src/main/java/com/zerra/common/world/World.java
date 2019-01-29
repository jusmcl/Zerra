package com.zerra.common.world;

import java.util.Random;
import java.util.Set;
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

public abstract class World
{
	protected Logger logger;
	protected String name;
	protected Random random;
	protected Vector2i worldSpawnPoint;

	protected ExecutorService pool;
	protected Layer[] layers;

	private long seed;

	protected World(String name, Long seed)
	{
		this.logger = LogManager.getLogger("World-" + name + "/" + (isServer() ? "Server" : "Client"));
		this.name = name;
		this.random = new Random();
		this.setSeed(seed);

		this.worldSpawnPoint = new Vector2i(random.nextInt(1024) - 512, random.nextInt(1024) - 512);

		this.pool = Executors.newCachedThreadPool(r -> new Thread(r, getClass().getSimpleName()));
		this.layers = new Layer[6];
		for (int i = 0; i < this.layers.length; i++)
		{
			this.layers[i] = new WorldLayer(this, i);
		}
	}

	protected World(String name)
	{
		this(name, null);
	}

	public void schedule(Runnable command)
	{
		this.pool.execute(command);
	}

	public void update()
	{
		for (int i = 0; i < this.layers.length; i++)
		{
			Set<Entity> entities = this.getLayer(i).getEntities();
			for (Entity entity : entities)
			{
				entity.update();
			}
		}
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
			entity.spawn();
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

	public abstract boolean isServer();

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

	public void setSeed(Long seed)
	{
		this.seed = seed == null ? random.nextLong() : seed;
		this.random.setSeed(this.seed);
	}

	public long getSeed()
	{
		return this.seed;
	}
}