package com.zerra.common.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.zerra.client.ZerraClient;
import com.zerra.common.Reference;

public class Registry<T extends RegistryNameable>
{

	private static final Pattern NAME = Pattern.compile("^(\\w+):\\w+$");
	private final Class<T> type;
	private final Map<String, T> entries = new HashMap<>();

	public Registry(Class<T> type)
	{
		this.type = type;
	}

	/**
	 * Gets the type of this {@link Registry}
	 */
	public Class<T> getType()
	{
		return type;
	}

	/**
	 * Get an entry by its registry name
	 */
	public T get(String registryName)
	{
		return entries.get(registryName);
	}

	/**
	 * Gets all entries matching the {@link Predicate}
	 */
	public Set<T> get(Predicate<T> predicate)
	{
		return entries.values().stream().filter(predicate).collect(Collectors.toSet());
	}

	/**
	 * Gets all entries matching the {@link Predicate} and then re-maps them using
	 * the {@link Function}
	 */
	public <R> Set<R> get(Predicate<T> predicate, Function<T, R> mapper)
	{
		return entries.values().stream().filter(predicate).map(mapper).collect(Collectors.toSet());
	}

	/**
	 * Adds the {@link RegistryNameable} object to this registry if it is of the
	 * correct type
	 */
	public void add(RegistryNameable object)
	{
		if (!type.isInstance(object))
		{
			throw new RuntimeException(String.format("The given %s is not of type %s for this registry", object.getClass().getName(), type.getName()));
		}
		T obj = type.cast(object);

		// Validate registry name
		String registryName = obj.getRegistryName();
		Matcher matcher = NAME.matcher(registryName);
		if (!matcher.matches())
		{
			throw new RuntimeException(String.format("The given %s has an invalid registry name: %s", obj.getClass().getName(), registryName));
		}

		// Validate domain
		String domain = matcher.group(1);
		// TODO: Should probably find easier access to the loaded mods
		if (!domain.equals(Reference.DOMAIN) && !ZerraClient.getInstance().getModManager().doesDomainExist(domain))
		{
			throw new RuntimeException(String.format("The given %s has a domain that does not exist: %s", obj.getClass().getName(), domain));
		}

		entries.put(registryName, obj);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
		{
			return true;
		}
		if (!(obj instanceof Registry))
		{
			return false;
		}
		Registry<?> r = (Registry<?>) obj;
		return type.isAssignableFrom(r.type) || r.type.isAssignableFrom(type);
	}
}
