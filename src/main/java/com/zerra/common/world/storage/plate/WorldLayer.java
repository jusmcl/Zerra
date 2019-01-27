package com.zerra.common.world.storage.plate;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.joml.Vector3i;
import org.joml.Vector3ic;

import com.zerra.common.world.World;
import com.zerra.common.world.entity.Entity;
import com.zerra.common.world.storage.Layer;
import com.zerra.common.world.tile.Tiles;

public class WorldLayer implements Layer
{
	private World world;
	private int layer;
	private Map<Vector3ic, Plate> loadedPlates;
	private Set<Entity> loadedEntities;

	public WorldLayer(World world, int layer)
	{
		this.world = world;
		this.layer = layer;
		this.loadedPlates = new ConcurrentHashMap<Vector3ic, Plate>();
		this.loadedEntities = new HashSet<Entity>();
	}
	
	@Override
	public void addPlate(Vector3ic pos, Plate plate)
	{
		this.loadedPlates.put(pos, plate);
	}
	
	@Override
	public void removePlate(Vector3ic pos)
	{
		this.loadedPlates.remove(pos);
	}

	@Override
	public Plate generate(Vector3ic pos)
	{
		Plate plate = new Plate(this);
		plate.setPlatePos(new Vector3i(pos));
		plate.fill(0, () -> this.world.getRandom().nextInt(3) == 0 ? Tiles.STONE : this.world.getRandom().nextInt(2) == 0 ? Tiles.GRASS : Tiles.SAND);
		return plate;
	}

	@Override
	public World getWorld()
	{
		return this.world;
	}

	@Override
	public Plate[] getLoadedPlates()
	{
		return this.loadedPlates.values().toArray(new Plate[0]);
	}

	@Override
	public Set<Entity> getEntities()
	{
		return loadedEntities;
	}

	@Override
	public Set<Entity> getEntities(Plate plate)
	{
		return loadedEntities.stream().filter(entity -> plate.isInsidePlate(entity.getTilePosition(), entity.getLayerId())).collect(Collectors.toSet());
	}

	@Override
	public Entity getEntityByUUID(UUID uuid)
	{
		return loadedEntities.stream().filter(entity -> entity.getUUID().equals(uuid)).findFirst().orElse(null);
	}

	@Override
	public int getLayerId()
	{
		return layer;
	}

	@Override
	public Plate getPlate(Vector3ic pos)
	{
		return this.loadedPlates.get(pos);
	}

	@Override
	public boolean isPlateLoaded(Vector3ic pos)
	{
		return this.loadedPlates.containsKey(pos);
	}
}
