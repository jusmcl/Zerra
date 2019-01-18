package com.zerra.common.util;

import com.zerra.client.Zerra;
import com.zerra.common.registry.RegistryNameable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class Factory<T> implements RegistryNameable {

	protected String registryName;
	protected final Class<T> type;
	protected Class<?>[] parameters = new Class[]{String.class};

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
	 * Sets the constructor parameters to use when creating a new instance of this factory's {@link Factory#type}
	 */
	protected Factory setConstructorParameters(Class<?>... parameters) {
		this.parameters = parameters;
		return this;
	}

	/**
	 * Gets a new instance of this factory's {@link Factory#type} using the given constructor {@link Factory#parameters}
	 * Returns null if a new instance couldn't be created
	 */
	public T getNewInstance() {
		Constructor<T> constructor = null;
		try {
			constructor = this.type.getConstructor(this.parameters);
			return constructor.newInstance(this.registryName);
		} catch (NoSuchMethodException e) {
			Zerra.logger().error(String.format("The class %s does not have a constructor with the parameters: %s", this.type.getName(), Arrays.toString(this.parameters)), e);
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
