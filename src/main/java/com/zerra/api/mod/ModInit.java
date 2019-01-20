package com.zerra.api.mod;

import com.zerra.common.registry.Registries;
import com.zerra.common.registry.RegistryNameable;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * Used for registration in the init method of mods.
 * 
 * @author Bright_Spark
 */
public class ModInit {

    private final String domain;

    public ModInit(String domain) {
        this.domain = domain;
    }

    /**
     * Allows the registration of any object, so long as it has a valid registry.
     * 
     * @param object The object to register.
     */
    public <T extends RegistryNameable> void register(T object) {
        object.setDomain(domain);
        Registries.register(object);
    }
}
