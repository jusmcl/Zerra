package com.zerra.common.world.storage;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.joml.Vector3ic;

import com.zerra.common.world.World;
import com.zerra.common.world.data.WorldData;
import com.zerra.common.world.entity.Entity;
import com.zerra.common.world.storage.plate.Plate;

public interface Layer
{

	/**
	 * Gets the world this layer is in
	 *
	 * @return World
	 */
	World getWorld();

	/**
	 * Loads a plate either from file or creates a new one.
	 */
	void loadPlate(Vector3ic pos);

	/**
	 * Unloads the plate and writes it to file.
	 */
	void unloadPlate(Vector3ic pos);

	/**
	 * @return The layer this is in the world
	 */
	int getLayerId();

	/**
	 * @return All of the plates currently loaded
	 */
	Plate[] getLoadedPlates();

	/**
	 * Gets all of the entities currently loaded in this layer
	 *
	 * @return All entities loaded
	 */
	Set<Entity> getEntities();

	/**
	 * Gets the entities loaded on the given plate
	 *
	 * @param plate the plate
	 * @return All entities currently on the plate
	 */
	Set<Entity> getEntities(Plate plate);

	/**
	 * Gets an entity by its UUID from this layer Retuns null if no entity exists
	 * with this UUID
	 *
	 * @param uuid Unique ID
	 * @return Entity with the UUID
	 */
	Entity getEntityByUUID(UUID uuid);

	/**
	 * Checks the loaded plates for the plate with the specified pos and loads it if
	 * it can not be found.
	 * 
	 * @param pos The pos to check
	 * @return The plate found or null if it is loading
	 */
	Plate getPlate(Vector3ic pos);

	/**
	 * Checks to see it a plate is loaded at the specified pos.
	 * 
	 * @param pos The pos to check
	 * @return Whether or not the plate is loaded at the pos
	 */
	boolean isPlateLoaded(Vector3ic pos);

	/**
	 * Gets all {@link WorldData} in this layer
	 */
	Map<String, WorldData> getAllWorldData();

	/**
	 * Gets {@link WorldData} by registry name in this layer
	 */
	WorldData getWorldData(String registryName);
}
