package com.zerra.api.mod.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Conf
{

	@Retention(RetentionPolicy.RUNTIME)
	@interface Doc
	{
		String value();
	}

	@Retention(RetentionPolicy.RUNTIME)
	@interface Range
	{
		int min();

		int max();
	}
}
