package com.zerra.common.world;

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
import com.zerra.common.world.entity.Entity;
import com.zerra.common.world.storage.IOManager.WorldStorageManager;
import com.zerra.common.world.storage.Layer;
import com.zerra.common.world.storage.plate.Plate;
import com.zerra.common.world.storage.plate.WorldLayer;

public class World {

	private Logger logger;
	private String name;
	private Random random;
	private Layer[] layers;
	private WorldStorageManager storageManager;
	private ExecutorService pool;
	
	private Vector2i worldSpawnPoint;

	public World(String name) {
		this.logger = LogManager.getLogger("World-" + name);
		this.name = name;
		this.random = new Random();

		// TODO: Make this also accept user-inputed seeds.
		this.random.setSeed(random.nextLong());

		worldSpawnPoint = new Vector2i(random.nextInt(1024) - 512, random.nextInt(1024) - 512);
		
		this.layers = new Layer[6];
		for (int i = 0; i < 6; i++) {
			this.layers[i] = new WorldLayer(this, i);
		}
		this.storageManager = new WorldStorageManager(this);
		this.pool = Executors.newCachedThreadPool();
	}

	public void schedule(Runnable command) {
		this.pool.execute(command);
	}

	public void stop() {
		for (int i = 0; i < this.layers.length; i++) {
			int layerId = i;
			this.pool.execute(() -> this.save(layerId));
		}
		this.pool.shutdown();
	}

	public void save(int layerId) {
		long startTime = System.currentTimeMillis();
		Layer layer = this.getLayer(layerId);
		if (layer != null) {
			for (Plate plate : layer.getLoadedPlates()) {
				if (plate != null) {
					save(layer, plate);
				}
			}
		}
		this.logger.info("Saved layer '{}' in {}", layerId, MiscUtils.secondsSinceTime(startTime));
	}

    public void save(int layerId, Vector3ic pos) {
		Layer layer = this.getLayer(layerId);
		if (layer != null) {
			if (layer.isPlateLoaded(pos)) {
                save(layer, Objects.requireNonNull(layer.getPlate(pos)));
			}
		}
	}

	private void save(@Nonnull Layer layer, @Nonnull Plate plate) {
		int layerId = layer.getLayerId();
		this.storageManager.writeEntitiesSafe(layerId, plate.getPlatePos(), layer.getEntities(plate));
		this.storageManager.writePlateSafe(layerId, plate);
	}

	@Nullable
    public Plate loadPlate(int layerId, Vector3ic pos) {
		Layer layer = this.getLayer(layerId);
		if (layer != null && layer.isPlateLoaded(pos)) {
			return this.storageManager.readPlateSafe(layerId, pos);
		}
		return null;
	}

	@Nullable
    public Set<Entity> loadEntities(int layerId, Vector3ic platePos) {
		Layer layer = this.getLayer(layerId);
		if(layer != null && layer.isPlateLoaded(platePos)) {
			return this.storageManager.readEntitiesSafe(layer.getLayerId(), platePos);
		}
		return null;
	}

    @Nullable
    public Entity getEntityByUUID(UUID uuid) {
        for (Layer layer : getLayers()) {
            Entity entity = layer.getEntityByUUID(uuid);
            if (entity != null) {
                return entity;
            }
        }
        return null;
    }

	public Logger logger() {
		return logger;
	}

	public String getName() {
		return name;
	}

	public Random getRandom() {
		return random;
	}

	public Layer[] getLayers() {
		return layers;
	}
	
	public Vector2i getWorldSpawnPoint() {
		return this.worldSpawnPoint;
	}

	public Layer getLayer(int layer) {
		if (layer < 0 || layer >= this.layers.length)
			return null;
		return this.layers[layer];
	}

	public WorldStorageManager getStorageManager() {
		return storageManager;
	}
}