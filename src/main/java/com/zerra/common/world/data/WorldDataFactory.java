package com.zerra.common.world.data;

import java.util.function.Predicate;

import com.zerra.common.util.Factory;
import com.zerra.common.world.World;

/**
 * A factory class used to create new {@link WorldData} instances and some
 * methods to determine what the instances should be created and used for
 */
public class WorldDataFactory<T extends WorldData> extends Factory<T>
{

	private Predicate<Integer> layerPredicate = null;
	private boolean perLayer = false;

	public WorldDataFactory(String registryName, Class<T> worldDataClass)
	{
		super(registryName, worldDataClass);
	}

	/**
	 * Sets a boolean so that the {@link WorldData} instance is created per
	 * {@link com.zerra.common.world.storage.Layer} rather than just per
	 * {@link World}
	 *
	 * @return This {@link WorldDataFactory}
	 */
	public WorldDataFactory<T> setIsPerLayer()
	{
		perLayer = true;
		return this;
	}

	/**
	 * Returns whether the {@link WorldData} should have an instance per
	 * {@link com.zerra.common.world.storage.Layer} or per {@link World}
	 *
	 * @return True if per {@link com.zerra.common.world.storage.Layer}, False if
	 *         per {@link World}
	 */
	public boolean isPerLayer()
	{
		return perLayer;
	}

	/**
	 * Sets a layer {@link Predicate<Integer>} which should return true if an
	 * instance of the {@link WorldData} should be created for the given layer ID
	 *
	 * @param layerPredicate Predicate to check if the {@link WorldData} should be
	 *        created for the given layer ID
	 * @return This {@link WorldDataFactory}
	 */
	public WorldDataFactory<T> setLayerPredicate(Predicate<Integer> layerPredicate)
	{
		this.layerPredicate = layerPredicate;
		return this;
	}

	/**
	 * If {@link WorldDataFactory#isPerLayer()} returns true, then this method is
	 * called for the layer to check if an instance of this {@link WorldData} should
	 * be created
	 *
	 * @param layer Layer ID
	 * @return True if should be created for the layer
	 */
	public boolean isForLayer(int layer) {
		return layerPredicate != null && layerPredicate.test(layer);
	}
}
