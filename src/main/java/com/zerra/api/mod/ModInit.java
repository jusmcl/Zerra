package com.zerra.api.mod;

import com.zerra.common.registry.RegistryNameable;

public class ModInit {

    private final String domain;

    public ModInit(String domain) {
        this.domain = domain;
    }

    public <T extends RegistryNameable> void register(T object) {
        object.setDomain(domain);
        //TODO: Register where necessary
    }
}
