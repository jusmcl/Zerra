package com.zerra.common.world.storage.plate;

import com.zerra.common.world.storage.Layer;
import com.zerra.common.world.tile.Tile;
import org.joml.Vector2i;
import org.joml.Vector3i;

import java.util.Arrays;
import java.util.function.Supplier;

public class Plate {
	
	public static final int SIZE = 64;

	private Tile[] tiles;
	private Vector3i platePos;
	private Layer layer;
	private boolean requiresRenderUpdate;
	private boolean loaded;

	public Plate(Layer layer) {
		this.tiles = new Tile[SIZE * SIZE];
		Arrays.fill(tiles, Tile.NONE);
		this.layer = layer;
		this.requiresRenderUpdate = false;
		this.loaded = true;
	}

	private static int posToIndex(Vector2i position)
	{
		return (position.x % SIZE) + (position.y % SIZE) * SIZE;
	}

	public void fill(int y, Supplier<Tile> toFill) {
		for (int i = 0; i < tiles.length; i++) {
			setTileAt(i, toFill.get());
		}
	}

	public boolean isInsidePlate(Vector3i tilePos) {
		int x = tilePos.x / SIZE;
		int z = tilePos.z / SIZE;
		return this.platePos.x == x && this.platePos.y == z && this.platePos.y == tilePos.y;
	}

	public Tile getTileAt(Vector2i position) {
		return tiles[posToIndex(position)];
	}

	public Vector3i getPlatePos() {
		return platePos;
	}

	public Layer getLayer() {
		return layer;
	}

	public boolean requiresRenderUpdate() {
		return requiresRenderUpdate;
	}
	
	public boolean isLoaded() {
		return loaded;
	}

	public void setPlatePos(Vector3i platePos) {
		this.platePos = platePos;
	}

	public void setTileAt(Vector2i position, Tile toPlace) {
		setTileAt(posToIndex(position), toPlace);
	}

	private void setTileAt(int index, Tile toPlace) {
		if (toPlace != Tile.NONE) {
			tiles[index].spawnDropsInLayer(layer);
		}
		tiles[index] = toPlace;
	}

	public void setRequiresRenderUpdate() {
		this.requiresRenderUpdate = true;
	}
	
	public void load() {
		this.loaded = true;
	}
	
	public void unload() {
		this.loaded = false;
	}

	@Override
	public int hashCode() {
		return 31 * 1 + Integer.hashCode(this.layer.getLayerId()) + 31 * this.platePos.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Plate) {
			return this.platePos.equals(((Plate) obj).platePos);
		}
		return super.equals(obj);
	}
}
