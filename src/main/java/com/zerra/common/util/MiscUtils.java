package com.zerra.common.util;

import com.zerra.common.Zerra;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class MiscUtils
{
	public static String millisSinceTime(long millis)
	{
		return System.currentTimeMillis() - millis + " ms";
	}

	public static String secondsSinceTime(long millis)
	{
		return (float) (System.currentTimeMillis() - millis) / 1000F + " seconds.";
	}

	public static <T> T createNewInstance(Class<T> clazz)
	{
		return createNewInstance(clazz, null);
	}

	public static <T> T createNewInstance(Class<T> clazz, @Nullable Class<?>[] parameterTypes, @Nullable Object... arguments)
	{
		Constructor<T> constructor = null;
		try
		{
			constructor = clazz.getDeclaredConstructor(parameterTypes);
			return constructor.newInstance(arguments);
		}
		catch(NoSuchMethodException e)
		{
			Zerra.logger().error(String.format("The class %s does not have a constructor with the parameter types: %s!", clazz.getName(), Arrays.toString(parameterTypes)), e);
		}
		catch(IllegalAccessException e)
		{
			Zerra.logger().error(String.format("Cannot access the constructor %s!", constructor), e);
		}
		catch(InstantiationException e)
		{
			Zerra.logger().error(String.format("Cannot instantiate the class %s because it is abstract!", clazz.getName()), e);
		}
		catch(InvocationTargetException e)
		{
			Zerra.logger().error(String.format("The constructor %s threw an exception!", constructor), e);
		}
		return null;
	}
}
