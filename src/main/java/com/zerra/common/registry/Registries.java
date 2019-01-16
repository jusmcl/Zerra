package com.zerra.common.registry;

import com.zerra.client.Zerra;
import com.zerra.common.world.data.WorldData;
import com.zerra.common.world.item.Item;
import com.zerra.common.world.tile.TileType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class Registries {

    private static final Set<Registry<? extends RegistryNameable>> REGISTRIES = new HashSet<>();

    static {
        addRegistry(new Registry<>(Item.class));
        addRegistry(new Registry<>(TileType.class));
		addRegistry(new Registry<>(WorldData.class));
    }

    public static void addRegistry(Registry<? extends RegistryNameable> registry) {
        REGISTRIES.add(registry);
    }

    public static <T extends RegistryNameable> void register(T object) {
        for (Registry<? extends RegistryNameable> registry : REGISTRIES) {
            Class<? extends RegistryNameable> type = registry.getType();
            if (type.isInstance(object)) {
                registry.add(object);
                return;
			}
		}
        Zerra.logger().warn("There is no registry for the type " + object.getClass().getName());
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	public static <T extends RegistryNameable> Registry<T> getRegistry(Class<T> type) {
		return (Registry<T>) REGISTRIES.stream()
			.filter(registry -> registry.getType().equals(type))
			.findFirst()
			.orElseThrow(() -> new RuntimeException(String.format("Registry of type %s does not exist!", type.getName())));
	}

	@Nullable
	public static <T extends RegistryNameable> T getRegisteredObject(String registryName, Class<T> type) {
		for (Registry<? extends RegistryNameable> registry : REGISTRIES) {
			if (registry.getType().equals(type)) {
				return type.cast(registry.get(registryName));
			}
		}
		return null;
	}
}
