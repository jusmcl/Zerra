package com.zerra.common.registry;

import java.util.HashSet;
import java.util.Set;

import com.zerra.client.ZerraClient;
import com.zerra.common.world.item.Item;
import com.zerra.common.world.tile.TileType;

public class Registries {

    private static final Set<Registry<? extends RegistryNameable>> REGISTRIES = new HashSet<>();

    static {
        addRegistry(new Registry<>(Item.class));
        addRegistry(new Registry<>(TileType.class));
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
        ZerraClient.logger().warn("There is no registry for the type " + object.getClass().getName());
	}
}
