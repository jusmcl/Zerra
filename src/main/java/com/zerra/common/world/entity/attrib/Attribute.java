package com.zerra.common.world.entity.attrib;

import com.zerra.common.world.storage.Storable;

public interface Attribute<T>
{
	String getRegistryName();
	
	T getValue();
}