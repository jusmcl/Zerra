package com.zerra.common.registry;

public interface RegistryNameable {

	static String injectDomain(String registryName, String domain) {
		return registryName.contains(":") ? registryName : domain + ":" + registryName;
	}

    void setDomain(String domain);

    String getRegistryName();
}
