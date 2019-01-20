package com.zerra.common.world;

import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2i;
import org.joml.Vector3ic;

import com.zerra.common.util.MiscUtils;
import com.zerra.common.world.data.WorldData;
import com.zerra.common.world.data.WorldDataHandler;
import com.zerra.common.world.entity.Entity;
import com.zerra.common.world.storage.IOManager.WorldStorageManager;
import com.zerra.common.world.storage.Layer;
import com.zerra.common.world.storage.plate.Plate;
import com.zerra.common.world.storage.plate.WorldLayer;

public class World
{

	private Logger logger;
	private String name;
	private Random random;
	private Vector2i worldSpawnPoint;

	private Layer[] layers;
	private Map<String, WorldData> worldData;

	private WorldStorageManager storageManager;
	private ExecutorService pool;

	public World(String name)
	{
		this.logger = LogManager.getLogger("World-" + name);
		this.name = name;
		this.random = new Random();

		// TODO: Make this also accept user-inputed seeds.
		this.random.setSeed(random.nextLong());

		this.storageManager = new WorldStorageManager(this);

		worldSpawnPoint = new Vector2i(random.nextInt(1024) - 512, random.nextInt(1024) - 512);

		// Create and load WorldData for world
		this.worldData = WorldDataHandler.getDataForWorld();
		Map<String, WorldData> data = this.storageManager.readWorldDataSafe(null);
		this.worldData.putAll(data);

		this.layers = new Layer[6];
		for (int i = 0; i < 6; i++)
		{
			this.layers[i] = new WorldLayer(this, i);
		}

		this.pool = Executors.newCachedThreadPool();
	}

	public World(String name, long seed)
	{
		this(name);

		// Overrides the previous constructor's stuff.
		this.random.setSeed(seed);
		worldSpawnPoint = new Vector2i(random.nextInt(1024) - 512, random.nextInt(1024) - 512);
	}

	public void schedule(Runnable command)
	{
		this.pool.execute(command);
	}

	/**
	 * Shuts down the thread pool and saves all data
	 */
	public void stop()
	{
		this.storageManager.writeWorldDataSafe(null, this.worldData);
		for (int i = 0; i < this.layers.length; i++)
		{
			int layerId = i;
			this.pool.execute(() -> this.save(layerId));
		}
		this.pool.shutdown();
	}

	/**
	 * Save data in the layer
	 */
	public void save(int layerId)
	{
		long startTime = System.currentTimeMillis();
		Layer layer = this.getLayer(layerId);
		if (layer != null)
		{
			this.storageManager.writeWorldDataSafe(layerId, this.worldData);
			for (Plate plate : layer.getLoadedPlates())
			{
				if (plate != null)
				{
					save(layer, plate);
				}
			}
		}
		this.logger.info("Saved layer '{}' in {}", layerId, MiscUtils.secondsSinceTime(startTime));
	}

	/**
	 * Save plate at pos
	 */
	public void save(int layerId, Vector3ic pos)
	{
		Layer layer = this.getLayer(layerId);
		if (layer != null)
		{
			if (layer.isPlateLoaded(pos))
			{
				save(layer, Objects.requireNonNull(layer.getPlate(pos)));
			}
		}
	}

	/**
	 * Save plate
	 */
	private void save(@Nonnull Layer layer, @Nonnull Plate plate)
	{
		int layerId = layer.getLayerId();
		this.storageManager.writeEntitiesSafe(layerId, plate.getPlatePos(), layer.getEntities(plate));
		this.storageManager.writePlateSafe(layerId, plate);
	}

	/**
	 * Load plate
	 */
	@Nullable
	public Plate loadPlate(int layerId, Vector3ic pos)
	{
		Layer layer = this.getLayer(layerId);
		if (layer != null && layer.isPlateLoaded(pos))
		{
			return this.storageManager.readPlateSafe(layerId, pos);
		}
		return null;
	}

	/**
	 * Load entities in plate
	 */
	@Nullable
	public Set<Entity> loadEntities(int layerId, Vector3ic platePos)
	{
		Layer layer = this.getLayer(layerId);
		if (layer != null && layer.isPlateLoaded(platePos))
		{
			return this.storageManager.readEntitiesSafe(layer.getLayerId(), platePos);
		}
		return null;
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

	public WorldStorageManager getStorageManager()
	{
		return storageManager;
	}

	/**
	 * Gets {@link WorldData} by registry name in this world
	 */
	public WorldData getWorldData(String registryName)
	{
		return worldData.get(registryName);
	}

	/**
	 * Gets {@link WorldData} by registry name in a layer
	 */
	public WorldData getWorldDataInLayer(String registryName, int layerId)
	{
		Layer layer = getLayer(layerId);
		return layer == null ? null : layer.getWorldData(registryName);
	}
}