package com.zerra.common.world.storage.plate;

import java.util.function.Supplier;

import org.joml.Vector2i;
import org.joml.Vector3i;

import com.zerra.common.world.storage.Layer;
import com.zerra.common.world.tile.Tile;

public class Plate {

	private Tile[] tiles;
	private Vector3i platePos;
	private int size;
	private Layer layer;
	private boolean requiresRenderUpdate;

	public Plate(int size, Layer layer) {
		this.tiles = new Tile[size * size];
		for (int z = 0; z < size; z++) {
			for (int x = 0; x < size; x++) {
				tiles[x + z * size] = Tile.NONE;
			}
		}
		this.size = size;
		this.layer = layer;
		this.requiresRenderUpdate = false;
	}

	public void fill(int y, Supplier<Tile> toFill) {
		for (int x = 0; x < size; x++) {
			for (int z = 0; z < size; z++) {
				setTileAt(new Vector2i(x, z), toFill.get());
			}
		}
	}

	public boolean isInsidePlate(Vector2i tilePos, int y) {
		int x = tilePos.x / 100;
		int z = tilePos.y / 100;
		return this.platePos.x == x && this.platePos.y == z && this.platePos.y == y;
	}

	public Tile getTileAt(Vector2i position) {
		int x = position.x % size;
		int z = position.y % size;
		return tiles[x + z * this.size];
	}

	public Vector3i getPlatePos() {
		return platePos;
	}

	public int getSize() {
		return size;
	}

	public Layer getLayer() {
		return layer;
	}

	public boolean requiresRenderUpdate() {
		return requiresRenderUpdate;
	}

	public void setPlatePos(Vector3i platePos) {
		this.platePos = platePos;
	}

	public void setTileAt(Vector2i position, Tile toPlace) {
		int x = position.x % size;
		int z = position.y % size;
		Tile toReplace = getTileAt(position);
		if (toPlace != Tile.NONE) {
			toReplace.spawnDropsInLayer(layer);
		}
		this.tiles[x + z * this.size] = toPlace;
	}

	public void setRequiresRenderUpdate() {
		this.requiresRenderUpdate = true;
	}

	@Override
	public int hashCode() {
		return 31 + this.platePos.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Plate) {
			return this.platePos.equals(((Plate) obj).platePos);
		}
		return super.equals(obj);
	}
}
