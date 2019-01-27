package com.zerra.common.world.storage;

import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import org.joml.Vector3ic;

import com.zerra.common.world.World;
import com.zerra.common.world.entity.Entity;
import com.zerra.common.world.storage.plate.Plate;

public interface Layer
{
	/**
	 * Adds a plate at the specified pos
	 * 
	 * @param pos
	 *            The position of the plate
	 * @param plate
	 *            The plate to add
	 */
	void addPlate(Vector3ic pos, Plate plate);

	/**
	 * Removes the plate at the specified pos
	 * 
	 * @param pos
	 *            The position of the plate
	 */
	void removePlate(Vector3ic pos);

	/**
	 * Generates a new plate at the specified position
	 * 
	 * @param pos
	 *            The pos to generate the plate for
	 */
	Plate generate(Vector3ic pos);

	/**
	 * @return The world this layer is in
	 */
	World getWorld();

	/**
	 * @return The layer this is in the world
	 */
	int getLayerId();

	/**
	 * @return All of the plates currently loaded
	 */
	Plate[] getLoadedPlates();

	/**
	 * @return All of the entities currently loaded in this layer
	 */
	Set<Entity> getEntities();

	/**
	 * Gets the entities loaded on the given plate
	 *
	 * @param plate
	 *            the plate
	 * @return All entities currently on the plate
	 */
	Set<Entity> getEntities(Plate plate);

	/**
	 * Gets an entity by its UUID from this layer Retuns null if no entity exists with this UUID
	 *
	 * @param uuid
	 *            Unique ID
	 * @return Entity with the UUID
	 */
	Entity getEntityByUUID(UUID uuid);

	/**
	 * Checks the loaded plates for the plate with the specified pos.
	 * 
	 * @param pos
	 *            The pos to check
	 * @return The plate found or null if it is not loaded
	 */
	@Nullable
	Plate getPlate(Vector3ic pos);

	/**
	 * Checks to see it a plate is loaded at the specified pos.
	 * 
	 * @param pos
	 *            The pos to check
	 * @return Whether or not the plate is loaded at the pos
	 */
	boolean isPlateLoaded(Vector3ic pos);
}
