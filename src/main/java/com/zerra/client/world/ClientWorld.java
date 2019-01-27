package com.zerra.client.world;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.joml.Vector3i;
import org.joml.Vector3ic;

import com.zerra.client.util.ResourceLocation;
import com.zerra.common.world.World;
import com.zerra.common.world.storage.IOManager.WorldStorageManager;

public class ClientWorld extends World
{
	private List<Pair<Integer, ResourceLocation>> tileIndexes;
	private Map<ResourceLocation, Integer> tileMapper;
	private List<byte[]> awaitingPlates;

	public ClientWorld(String name, Long seed)
	{
		super(name, seed, false);
		this.tileIndexes = new ArrayList<Pair<Integer, ResourceLocation>>();
		this.tileMapper = new HashMap<ResourceLocation, Integer>();
		this.awaitingPlates = new ArrayList<byte[]>();
	}

	@Override
	public void update()
	{
		if (this.tileIndexes.isEmpty())
		{
			System.out.println(this.awaitingPlates);
		}
		
		if (!this.tileIndexes.isEmpty() && !this.awaitingPlates.isEmpty())
		{
			for (int i = 0; i < this.awaitingPlates.size(); i++)
			{
				byte[] bytes = this.awaitingPlates.get(i);
				try (DataInputStream is = new DataInputStream(new ByteArrayInputStream(bytes)))
				{
					int layer = is.readInt();
					Vector3ic platePos = new Vector3i(is.readInt(), is.readInt(), is.readInt());
					this.getLayer(layer).addPlate(platePos, WorldStorageManager.readPlate(is, this.getLayer(layer), platePos, this.tileIndexes));
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				this.awaitingPlates.remove(i);
				i--;
			}
		}
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

	public void processPlate(byte[] bytes)
	{
		this.awaitingPlates.add(bytes);
	}
}