package com.zerra.common.world.data;

import com.zerra.client.Zerra;
import com.zerra.common.registry.RegistryNameable;
import com.zerra.common.world.World;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Predicate;

/**
 * A factory class used to contain a getter for {@link WorldData} instances and some methods to determine what the
 * instances should be created and used for
 */
public class WorldDataFactory implements RegistryNameable {

	private String registryName;
	private final Class<? extends WorldData> worldDataClass;
	private Predicate<Integer> layerPredicate = null;
	private boolean perLayer = false;

	public WorldDataFactory(String registryName, Class<? extends WorldData> worldDataClass) {
		this.registryName = registryName;
		this.worldDataClass = worldDataClass;
	}

	@Override
	public void setDomain(String domain) {
		this.registryName = RegistryNameable.injectDomain(this.registryName, domain);
	}

	@Override
	public String getRegistryName() {
		return registryName;
	}

	/**
	 * Gets a new instance of the {@link WorldData}
	 * Returns null if a new instance couldn't be created
	 */
	public WorldData getNewInstance() {
		Constructor<? extends WorldData> constructor = null;
		try {
			constructor = worldDataClass.getConstructor(String.class);
			return constructor.newInstance(registryName);
		} catch (NoSuchMethodException e) {
			Zerra.logger().error(String.format("The WorldData class %s does not have a constructor with a single String argument!", worldDataClass.getName()), e);
		} catch (IllegalAccessException e) {
			Zerra.logger().error(String.format("Cannot access the constructor %s!", constructor), e);
		} catch (InstantiationException e) {
			Zerra.logger().error(String.format("Cannot instantiate the class %s because it is abstract!", worldDataClass.getName()), e);
		} catch (InvocationTargetException e) {
			Zerra.logger().error(String.format("The constructor %s threw an exception!", constructor), e);
		}
		return null;
	}

	/**
	 * Sets a boolean so that the {@link WorldData} instance is created per {@link com.zerra.common.world.storage.Layer}
	 * rather than just per {@link World}
	 *
	 * @return This {@link WorldDataFactory}
	 */
	public WorldDataFactory setIsPerLayer() {
		perLayer = true;
		return this;
	}

	/**
	 * Returns whether the {@link WorldData} should have an instance per {@link com.zerra.common.world.storage.Layer}
	 * or per {@link World}
	 *
	 * @return True if per {@link com.zerra.common.world.storage.Layer}, False if per {@link World}
	 */
	public boolean isPerLayer() {
		return perLayer;
	}

	/**
	 * Sets a layer {@link Predicate<Integer>} which should return true if an instance of the {@link WorldData} should
	 * be created for the given layer ID
	 *
	 * @param layerPredicate Predicate to check if the {@link WorldData} should be created for the given layer ID
	 * @return This {@link WorldDataFactory}
	 */
	public WorldDataFactory setLayerPredicate(Predicate<Integer> layerPredicate) {
		this.layerPredicate = layerPredicate;
		return this;
	}

	/**
	 * If {@link WorldDataFactory#isPerLayer()} returns true, then this method is called for the layer to check if an
	 * instance of this {@link WorldData} should be created
	 *
	 * @param layer Layer ID
	 * @return True if should be created for the layer
	 */
	public boolean isForLayer(int layer) {
		return layerPredicate == null || layerPredicate.test(layer);
	}
}
