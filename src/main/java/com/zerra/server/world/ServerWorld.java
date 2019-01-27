package com.zerra.server.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.joml.Vector3i;
import org.joml.Vector3ic;

import com.zerra.common.util.MiscUtils;
import com.zerra.common.world.World;
import com.zerra.common.world.data.WorldData;
import com.zerra.common.world.data.WorldDataHandler;
import com.zerra.common.world.entity.Entity;
import com.zerra.common.world.entity.EntityPlayer;
import com.zerra.common.world.storage.IOManager.WorldStorageManager;
import com.zerra.common.world.storage.Layer;
import com.zerra.common.world.storage.plate.Plate;

public class ServerWorld extends World
{
	private Map<String, WorldData> worldData;
	private List<Map<String, WorldData>> worldLayerData;
	private WorldStorageManager storageManager;

	private Map<Integer, List<Vector3ic>> loadingPlates;

	public ServerWorld(String name)
	{
		super(name, true);
		this.storageManager = new WorldStorageManager(this);

		// Create and load WorldData for world
		this.worldData = WorldDataHandler.getDataForWorld();
		this.worldData.putAll(this.storageManager.readWorldDataSafe(null));

		// Create and load WorldData for layers
		this.worldLayerData = new ArrayList<Map<String, WorldData>>();
		this.loadingPlates = new HashMap<Integer, List<Vector3ic>>();
		for (int i = 0; i < this.layers.length; i++)
		{
			Map<String, WorldData> worldData = WorldDataHandler.getDataForLayer(i);
			worldData.putAll(this.storageManager.readWorldDataSafe(i));
			this.worldLayerData.add(worldData);
			this.loadingPlates.put(i, new ArrayList<Vector3ic>());
		}
	}
	
	public void startup() {
		// Load 3x3 in the first layer
		for (int x = 0; x < 3; x++)
		{
			for (int z = 0; z < 3; z++)
			{
				Vector3i pos = new Vector3i(x - 1, 0, z - 1);
				this.loadPlateWait(0, pos);
			}
		}

		// TODO remove this temp code
		this.addEntity(new EntityPlayer(this));
	}

	@Override
	public void update()
	{
		// TODO: Update server world here.
	}

	@Override
	public void stop()
	{
		this.storageManager.writeWorldDataSafe(null, this.worldData);
		for (int i = 0; i < this.layers.length; i++)
		{
			int layerId = i;
			this.pool.execute(() -> this.save(layerId));
		}
		super.stop();
	}

	private void loadPlateWait(int layer, Vector3ic pos)
	{
		Vector3i platePos = new Vector3i(pos);
		Layer worldLayer = this.getLayer(layer);
		List<Vector3ic> loadingPlates = this.loadingPlates.get(layer);

		if (worldLayer != null && loadingPlates != null)
		{
			if (!loadingPlates.contains(platePos) && !worldLayer.isPlateLoaded(platePos))
			{
				loadingPlates.add(platePos);
				if (this.getStorageManager().isPlateGenerated(layer, platePos))
				{
					this.getStorageManager().readEntitiesSafe(layer, platePos).forEach((entity) -> worldLayer.getEntities().add(entity));
					worldLayer.addPlate(platePos, this.getStorageManager().readPlateSafe(layer, platePos));
					loadingPlates.remove(platePos);
					this.logger().info("Loaded plate at " + pos.x() + ", " + pos.y() + ", " + pos.z() + " in layer " + layer);
				}
				else
				{
					worldLayer.addPlate(platePos, worldLayer.generate(platePos));
					loadingPlates.remove(platePos);
					this.logger().info("Generated plate at " + pos.x() + ", " + pos.y() + ", " + pos.z() + " in layer " + layer);
				}
			}
		}
	}

	public void loadPlate(int layer, Vector3ic pos)
	{
		Vector3i platePos = new Vector3i(pos);
		Layer worldLayer = this.getLayer(layer);
		List<Vector3ic> loadingPlates = this.loadingPlates.get(layer);

		if (worldLayer != null && loadingPlates != null)
		{
			if (!loadingPlates.contains(platePos) && !worldLayer.isPlateLoaded(platePos))
			{
				loadingPlates.add(platePos);
				if (this.getStorageManager().isPlateGenerated(layer, platePos))
				{
					this.schedule(() ->
					{
						this.getStorageManager().readEntitiesSafe(layer, platePos).forEach((entity) -> worldLayer.getEntities().add(entity));
						worldLayer.addPlate(platePos, this.getStorageManager().readPlateSafe(layer, platePos));
						loadingPlates.remove(platePos);
						this.logger().info("Loaded plate at " + pos.x() + ", " + pos.y() + ", " + pos.z() + " in layer " + layer);
					});
				}
				else
				{
					this.schedule(() ->
					{
						worldLayer.addPlate(platePos, worldLayer.generate(platePos));
						loadingPlates.remove(platePos);
						this.logger().info("Generated plate at " + pos.x() + ", " + pos.y() + ", " + pos.z() + " in layer " + layer);
					});
				}
			}
		}
	}

	public void unloadPlate(int layer, Vector3ic pos)
	{
		this.logger().info("Unloaded plate at " + pos.x() + ", " + pos.y() + ", " + pos.z() + " in layer " + layer);
		Layer worldLayer = this.getLayer(layer);

		if (worldLayer != null)
		{
			Plate plate = worldLayer.getPlate(pos);

			if (plate != null)
			{
				plate.unload();
				this.schedule(() ->
				{
					this.save(layer, plate.getPlatePos());
					worldLayer.getEntities().removeIf(entity -> plate.isInsidePlate(entity.getTilePosition(), entity.getLayerId()));
					worldLayer.removePlate(pos);
				});
			}
		}
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
			this.storageManager.writeWorldDataSafe(layerId, this.getAllLayerWorldData(layerId));
			for (Plate plate : layer.getLoadedPlates())
			{
				if (plate != null)
				{
					this.save(layer, plate);
				}
			}
		}
		this.logger().info("Saved layer '{}' in {}", layerId, MiscUtils.secondsSinceTime(startTime));
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

	/**
	 * Save plate
	 */
	private void save(@Nonnull Layer layer, @Nonnull Plate plate)
	{
		int layerId = layer.getLayerId();
		this.storageManager.writeEntitiesSafe(layerId, plate.getPlatePos(), layer.getEntities(plate));
		this.storageManager.writePlateSafe(layerId, plate);
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
	 * Gets {@link WorldData} by registry name in this world on the specified layer
	 */
	@Nullable
	public WorldData getLayerWorldData(int layer, String registryName)
	{
		return layer < 0 || layer >= this.layers.length ? null : worldLayerData.get(layer).get(registryName);
	}

	/**
	 * Gets all {@link WorldData} in this world on the specified layer
	 */
	@Nullable
	public Map<String, WorldData> getAllLayerWorldData(int layer)
	{
		return layer < 0 || layer >= this.layers.length ? null : worldLayerData.get(layer);
	}
}