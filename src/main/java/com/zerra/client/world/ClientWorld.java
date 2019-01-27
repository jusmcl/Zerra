package com.zerra.client.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.zerra.client.util.ResourceLocation;
import com.zerra.common.world.World;
import com.zerra.common.world.entity.EntityPlayer;
import com.zerra.common.world.storage.IOManager.WorldStorageManager;

public class ClientWorld extends World
{
	private List<Pair<Integer, ResourceLocation>> tileIndexes;
	private Map<ResourceLocation, Integer> tileMapper;

	public ClientWorld(String name, Long seed)
	{
		super(name, seed, false);
		this.tileIndexes = new ArrayList<Pair<Integer, ResourceLocation>>();
		this.tileMapper = new HashMap<ResourceLocation, Integer>();

		// TODO remove this temp code and sync with server instead
		this.addEntity(new EntityPlayer(this));
	}

	@Override
	public void update()
	{
		// TODO: Update client world here.
	}
	
	public void render(float partialTicks) {
		
	}

	public void setTileIndexes(List<Pair<Integer, ResourceLocation>> tileIndexes)
	{
		this.tileIndexes.clear();
		this.tileIndexes.addAll(tileIndexes);
		this.tileMapper.clear();
		this.tileMapper.putAll(WorldStorageManager.createTileMapper(tileIndexes));
	}

	public List<Pair<Integer, ResourceLocation>> getTileIndexes()
	{
		return tileIndexes;
	}

	public Map<ResourceLocation, Integer> getTileMapper()
	{
		return tileMapper;
	}
}