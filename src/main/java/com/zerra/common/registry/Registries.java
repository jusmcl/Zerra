package com.zerra.common.registry;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.zerra.common.util.Factory;
import com.zerra.common.world.item.Item;
import com.zerra.common.world.tile.TileType;

public class Registries
{

	private static final Set<Registry<? extends RegistryNameable>> REGISTRIES = new HashSet<>();

	static
	{
		try
		{
			addRegistry(new Registry<>(Item.class));
			addRegistry(new Registry<>(TileType.class));
			addRegistry(new Registry<>(Factory.class));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Adds a new registry Will throw a {@link RuntimeException} if a registry
	 * already exists with a conflicting type
	 *
	 * @param registry New registry
	 */
	public static void addRegistry(Registry<? extends RegistryNameable> registry)
	{
		if (!REGISTRIES.add(registry))
		{
			throw new RuntimeException(String.format("A registry with a conflicting type already exists for the type %s!", registry.getType().getName()));
		}
	}

	/**
	 * Tries to register an object to a registry Will throw a
	 * {@link RuntimeException} if no registry could be found for the object
	 *
	 * @param object Object to register
	 * @param <T> Object type
	 */
	public static <T extends RegistryNameable> void register(T object)
	{
		for (Registry<? extends RegistryNameable> registry : REGISTRIES)
		{
			Class<? extends RegistryNameable> type = registry.getType();
			if (type.isInstance(object))
			{
				registry.add(object);
				return;
			}
		}
		throw new RuntimeException("There is no registry for the type " + object.getClass().getName());
	}

	/**
	 * Gets a registry by its type Will throw a {@link RuntimeException} if no
	 * registry could be found
	 *
	 * @param type The registry class type
	 * @param <T> The registry type
	 * @return The registry
	 */
	@SuppressWarnings("unchecked")
	@Nonnull
	public static <T extends RegistryNameable> Registry<T> getRegistry(Class<T> type)
	{
		return (Registry<T>) REGISTRIES.stream().filter(registry -> registry.getType().equals(type)).findFirst()
				.orElseThrow(() -> new RuntimeException(String.format("Registry of type %s does not exist!", type.getName())));
	}

	/**
	 * Gets a registered object by its registry name and type
	 *
	 * @param registryName Registry name
	 * @param type Object class
	 * @param <T> Type of the object
	 * @return The registered object or null if couldn't be found
	 */
	@Nullable
	public static <T extends RegistryNameable> T getRegisteredObject(String registryName, Class<T> type)
	{
		for (Registry<? extends RegistryNameable> registry : REGISTRIES)
		{
			if (registry.getType().equals(type))
			{
				return type.cast(registry.get(registryName));
			}
		}
		return null;
	}

	/**
	 * Gets a new instance from the registered factory Will throw a
	 * {@link RuntimeException} if no registered factory could be found
	 *
	 * @param registryName Registry name of the factory
	 * @return New instance
	 */
	public static Object getNewInstanceFromFactory(String registryName)
	{
		Factory<?> factory = getRegisteredObject(registryName, Factory.class);
		if (factory != null)
		{
			return factory.getNewInstance();
		}
		throw new RuntimeException(String.format("There is no factory registered with registry name %s", registryName));
	}

	/**
	 * Gets a new instance of the instance type specified if there's a factory for
	 * it registered Will throw a {@link RuntimeException} if no registered factory
	 * could be found
	 *
	 * @param registryName Registry name of the factory
	 * @param instanceType Factory new instance type
	 * @param <T> Type of the new instance
	 * @return New instance
	 */
	public static <T> T getNewInstanceFromFactory(String registryName, Class<T> instanceType)
	{
		Object instance = getNewInstanceFromFactory(registryName);
		if (instanceType.isInstance(instance))
		{
			return instanceType.cast(instance);
		} else
		{
			throw new RuntimeException(String.format("Factory found for registry name '%s', but it's of the wrong type: %s", registryName, instance.getClass().getName()));
		}
	}
}
