package com.zerra.common.registry;

import com.zerra.client.Zerra;
import com.zerra.common.world.item.Item;

import java.util.HashSet;
import java.util.Set;

public class Registries {

    private static final Set<Registry<? extends RegistryNameable>> REGISTRIES = new HashSet<>();

    static {
        addRegistry(new Registry<>(Item.class));
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
}
