package com.zerra.common.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.zerra.common.Zerra;
import com.zerra.common.registry.RegistryNameable;

public class Factory<T> implements RegistryNameable {

	protected String registryName;
	protected final Class<T> type;

	public Factory(String registryName, Class<T> type) {
		this.registryName = registryName;
		this.type = type;
	}

	@Override
	public void setDomain(String domain) {
		this.registryName = RegistryNameable.injectDomain(this.registryName, domain);
	}

	@Override
	public String getRegistryName() {
		return this.registryName;
	}

	public Class<T> getType() {
		return this.type;
	}

	/**
	 * Gets a new instance of this factory's {@link Factory#type} using the given constructor {@link Factory#parameters}
	 * Returns null if a new instance couldn't be created
	 */
	public T getNewInstance() {
		Constructor<T> constructor = null;
		try {
			constructor = this.type.getConstructor(String.class);
			return constructor.newInstance(this.registryName);
		} catch (NoSuchMethodException e) {
			Zerra.logger().error(String.format("The class %s does not have a constructor with a single String paramter!", this.type.getName()), e);
		} catch (IllegalAccessException e) {
			Zerra.logger().error(String.format("Cannot access the constructor %s!", constructor), e);
		} catch (InstantiationException e) {
			Zerra.logger().error(String.format("Cannot instantiate the class %s because it is abstract!", this.type.getName()), e);
		} catch (InvocationTargetException e) {
			Zerra.logger().error(String.format("The constructor %s threw an exception!", constructor), e);
		}
		return null;
	}
}
