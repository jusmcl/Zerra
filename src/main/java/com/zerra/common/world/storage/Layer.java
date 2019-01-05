package com.zerra.common.world.storage;

import javax.annotation.Nullable;

import org.joml.Vector3i;

import com.zerra.common.world.entity.Entity;
import com.zerra.common.world.storage.plate.Plate;

public interface Layer {

	/**
	 * Loads a plate either from file or creates a new one.
	 */
	void loadPlate(Vector3i pos);

	/**
	 * Unloads the plate and writes it to file.
	 */
	void unloadPlate(Vector3i pos);
	
	/**
	 * @return The layer this is in the world
	 */
	int getLayer();

	/**
	 * @return All of the plates currently loaded
	 */
	Plate[] getLoadedPlates();

	Entity[] getEntities();

	/**
	 * Checks the loaded plates for the plate with the specified pos and loads it if it can not be found.
	 * 
	 * @param pos
	 *            The pos to check
	 * @return The plate found or null if it is loading
	 */
	@Nullable
	Plate getPlate(Vector3i pos);

	/**
	 * Checks to see it a plate is loaded at the specified pos.
	 * 
	 * @param pos
	 *            The pos to check
	 * @return Whether or not the plate is loaded at the pos
	 */
	boolean isPlateLoaded(Vector3i pos);
}
