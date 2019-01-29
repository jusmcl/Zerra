package com.zerra.common.world.storage.plate;

import java.util.Arrays;
import java.util.function.Supplier;

import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.joml.Vector3i;
import org.joml.Vector3ic;

import com.zerra.common.world.storage.Layer;
import com.zerra.common.world.tile.Tile;

public class Plate
{

	public static final int SIZE = 64;

	private Tile[] tiles;
	private Vector3i platePos;
	private Layer layer;
	private boolean requiresRenderUpdate;
	private boolean loaded;

	public Plate(Layer layer)
	{
		this.tiles = new Tile[SIZE * SIZE];
		Arrays.fill(tiles, Tile.NONE);
		this.layer = layer;
		this.requiresRenderUpdate = false;
		this.loaded = true;
	}

	private static int posToIndex(Vector2ic position)
	{
		return (position.x() % SIZE) + (position.y() % SIZE) * SIZE;
	}

	public void fill(int y, Supplier<Tile> toFill)
	{
		for (int i = 0; i < tiles.length; i++)
		{
			setTileAt(i, toFill.get());
		}
	}

	public boolean isInsidePlate(Vector3ic tilePos, int layer)
	{
		int x = tilePos.x() / SIZE;
		int y = tilePos.y() / SIZE;
		int z = tilePos.z() / SIZE;
		return this.platePos.x == x && this.platePos.y == y && this.platePos.z == z && this.layer.getLayerId() == layer;
	}

	public Tile getTileAt(Vector2ic position)
	{
		return tiles[posToIndex(position)];
	}

	public Vector3ic getPlatePos()
	{
		return platePos;
	}

	public Layer getLayer()
	{
		return layer;
	}

	public boolean requiresRenderUpdate()
	{
		return requiresRenderUpdate;
	}

	public boolean isLoaded()
	{
		return loaded;
	}

	public void setPlatePos(Vector3ic platePos)
	{
		if (this.platePos == null)
		{
			this.platePos = new Vector3i(platePos);
		}
		else
		{
			this.platePos.set(platePos);
		}
	}

	public void setTileAt(Vector2i position, Tile toPlace)
	{
		setTileAt(posToIndex(position), toPlace);
	}

	private void setTileAt(int index, Tile toPlace)
	{
		if (toPlace != Tile.NONE)
		{
			tiles[index].spawnDropsInLayer(layer);
		}
		tiles[index] = toPlace;
	}

	public void setRequiresRenderUpdate()
	{
		this.requiresRenderUpdate = true;
	}

	public void load()
	{
		this.loaded = true;
	}

	public void unload()
	{
		this.loaded = false;
	}

	@Override
	public int hashCode()
	{
		return 31 * 1 + Integer.hashCode(this.layer.getLayerId()) + 31 * this.platePos.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Plate)
		{
			return this.platePos.equals(((Plate) obj).platePos);
		}
		return super.equals(obj);
	}

	// FIXME
	// public byte[] toBytes()
	// {
	//
	// ByteArrayOutputStream bos = new ByteArrayOutputStream();
	//
	// List<Pair<Integer, ResourceLocation>> indexes = layer.getWorld().getStorageManager().getTileIndexes();
	// Map<ResourceLocation, Integer> mapper = layer.getWorld().getStorageManager().getTileMapper();
	//
	// ObjectOutput out = null;
	// try
	// {
	// out = new ObjectOutputStream(bos);
	// } catch (IOException e1)
	// {
	// e1.printStackTrace();
	// }
	// for (int x = 0; x < Plate.SIZE; x++)
	// {
	// for (int z = 0; z < Plate.SIZE; z++)
	// {
	// Vector2i position = new Vector2i(x, z);
	// try
	// {
	// out.writeShort(indexes.get(mapper.get(this.getTileAt(position).getRegistryID())).getLeft());
	// } catch (IOException e)
	// {
	// e.printStackTrace();
	// }
	//
	// }
	// }
	// return bos.toByteArray();
	// }
	//
	// public Plate fromBytes(byte[] bytes)
	// {
	// List<Pair<Integer, ResourceLocation>> indexes = layer.getWorld().getStorageManager().getTileIndexes();
	//
	// DataInputStream is = new DataInputStream(new ByteArrayInputStream(bytes));
	//
	// Plate plate = new Plate(this.layer);
	// for (int x = 0; x < Plate.SIZE; x++)
	// {
	// for (int z = 0; z < Plate.SIZE; z++)
	// {
	// Vector2i tilePos = new Vector2i(x, z);
	// try
	// {
	// plate.setTileAt(tilePos, Tiles.byId(indexes.get(is.readShort()).getRight()));
	// } catch (IOException e)
	// {
	// e.printStackTrace();
	// }
	// }
	// }
	// plate.setPlatePos(platePos);
	//
	// return plate;
	// }
}
