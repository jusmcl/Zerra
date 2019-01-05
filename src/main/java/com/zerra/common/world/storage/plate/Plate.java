package com.zerra.common.world.storage.plate;

import java.util.function.Supplier;

import org.joml.Vector2i;
import org.joml.Vector3i;

import com.zerra.common.world.storage.Layer;
import com.zerra.common.world.tile.Tile;

public class Plate {
	
	public static final int SIZE = 64;

	private Tile[] tiles;
	private Vector3i platePos;
	private Layer layer;
	private boolean requiresRenderUpdate;
	private boolean loaded;

	public Plate(Layer layer) {
		this.tiles = new Tile[SIZE * SIZE];
		for (int z = 0; z < SIZE; z++) {
			for (int x = 0; x < SIZE; x++) {
				tiles[x + z * SIZE] = Tile.NONE;
			}
		}
		this.layer = layer;
		this.requiresRenderUpdate = false;
		this.loaded = true;
	}

	public void fill(int y, Supplier<Tile> toFill) {
		for (int x = 0; x < SIZE; x++) {
			for (int z = 0; z < SIZE; z++) {
				setTileAt(new Vector2i(x, z), toFill.get());
			}
		}
	}

	public boolean isInsidePlate(Vector2i tilePos, int y) {
		int x = tilePos.x / SIZE;
		int z = tilePos.y / SIZE;
		return this.platePos.x == x && this.platePos.y == z && this.platePos.y == y;
	}

	public Tile getTileAt(Vector2i position) {
		int x = position.x % SIZE;
		int z = position.y % SIZE;
		return tiles[x + z * SIZE];
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
		int x = position.x % SIZE;
		int z = position.y % SIZE;
		Tile toReplace = getTileAt(position);
		if (toPlace != Tile.NONE) {
			toReplace.spawnDropsInLayer(layer);
		}
		this.tiles[x + z * SIZE] = toPlace;
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
		return 31 * 1 + Integer.hashCode(this.layer.getLayer()) + 31 * this.platePos.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Plate) {
			return this.platePos.equals(((Plate) obj).platePos);
		}
		return super.equals(obj);
	}
}
