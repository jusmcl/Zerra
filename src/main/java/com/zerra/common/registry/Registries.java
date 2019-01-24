package com.zerra.common.registry;

import com.zerra.common.Zerra;
import com.zerra.common.network.Message;
import com.zerra.common.util.Factory;
import com.zerra.common.util.MiscUtils;
import com.zerra.common.world.item.Item;
import com.zerra.common.world.tile.TileType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Registries
{
	private static final Set<Registry<? extends RegistryNameable>> REGISTRIES = new HashSet<>();

	private static final Map<Integer, Class<? extends Message>> MESSAGES = new HashMap<>();
	private static final Map<String, Integer> NUM_MESSAGES_BY_DOMAIN = new HashMap<>();

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

	/**
	 * Registers a {@link Message} class
	 *
	 * @param domain Mod domain name
	 * @param message Message instance
	 */
	public static <T extends Message> void registerMessage(String domain, Class<T> message)
	{
		if (StringUtils.isEmpty(domain))
		{
			throw new RuntimeException("Can't register Message with null or empty domain!");
		}
		if (MESSAGES.containsValue(message))
		{
			throw new RuntimeException(String.format("The message class %s has already been registered!", message.getName()));
		}
		//Get the next message ID for this domain
		int nextId = NUM_MESSAGES_BY_DOMAIN.getOrDefault(domain, -1) + 1;
		//Create hash for message
		int hash = new HashCodeBuilder().append(domain).append(nextId).toHashCode();
		if (MESSAGES.containsKey(hash))
		{
			throw new RuntimeException(String.format("The hash %s generated for the message class %s already exists!", hash, message.getName()));
		}
		//Add the new message
		MESSAGES.put(hash, message);
		NUM_MESSAGES_BY_DOMAIN.put(domain, nextId);
	}

	/**
	 * Gets a new {@link Message} instance for the given ID
	 * Returns null if no message registered for the ID
	 *
	 * @param id Message registry ID
	 */
	@Nullable
	public static Message getMessage(int id)
	{
		Class<? extends Message> messageClass = MESSAGES.get(id);
		if (messageClass == null)
		{
			Zerra.logger().warn("No message for id: " + id);
			return null;
		}
		return MiscUtils.createNewInstance(messageClass).setId(id);
	}
}
