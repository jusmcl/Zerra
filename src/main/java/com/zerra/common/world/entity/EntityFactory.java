package com.zerra.common.world.entity;

import java.util.function.Predicate;

import com.zerra.common.util.Factory;
import com.zerra.common.world.tile.Tile;

/**
 * A factory class used to create new {@link T} instances and some methods to determine when and where this entity
 * can spawn
 */
public class EntityFactory<T extends Entity> extends Factory<T> {

	private Predicate<Integer> layerPredicate = null;
	private Predicate<Tile> tilePredicate = null;

	public EntityFactory(String registryName, Class<T> entityClass) {
		super(registryName, entityClass);
	}

	/**
	 * Sets a layer {@link Predicate<Integer>} which should return true if an instance of the {@link T} should
	 * be created for the given layer ID
	 *
	 * @param layerPredicate Predicate to check if the {@link T} should be created for the given layer ID
	 * @return This {@link EntityFactory}
	 */
	public EntityFactory setLayerPredicate(Predicate<Integer> layerPredicate) {
		this.layerPredicate = layerPredicate;
		return this;
	}

	/**
	 * This method is called for the layer to check if an instance of this {@link T} should be created
	 *
	 * @param layer Layer ID
	 * @return True if should be created for the layer
	 */
	public boolean validForLayer(int layer) {
		return layerPredicate == null || layerPredicate.test(layer);
	}

	/**
	 * Sets a tile {@link Predicate<Tile>} which should return true if an instance of the {@link T} should
	 * be created for the given tile
	 *
	 * @param tilePredicate Predicate to check if the {@link T} should be created for the given tile
	 * @return This {@link EntityFactory}
	 */
	public EntityFactory setTilePredicate(Predicate<Tile> tilePredicate) {
		this.tilePredicate = tilePredicate;
		return this;
	}

	/**
	 * This method is called for the tile to check if an instance of this {@link T} should be created
	 *
	 * @param tile Tile
	 * @return True if valid for the tile
	 */
	public boolean validForTile(Tile tile) {
		return tilePredicate == null || tilePredicate.test(tile);
	}
}
