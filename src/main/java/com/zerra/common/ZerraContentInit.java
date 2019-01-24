package com.zerra.common;

import com.zerra.common.network.Message;
import com.zerra.common.network.msg.*;
import com.zerra.common.registry.Registries;
import com.zerra.common.registry.RegistryNameable;

public class ZerraContentInit
{
	private static <T extends RegistryNameable> void reg(T object)
	{
		object.setDomain(Reference.DOMAIN);
		Registries.register(object);
	}

	private static <T extends Message> void regMessage(Class<T> message)
	{
		Registries.registerMessage(Reference.DOMAIN, message);
	}

	public static void init()
	{
		//TODO: Move all base game content initialisation into here

		//Messages
		regMessage(MessageBadRequest.class);
		regMessage(MessageConnect.class);
		regMessage(MessageDisconnect.class);
		regMessage(MessageEntityMove.class);
		regMessage(MessagePing.class);
		regMessage(MessagePlateData.class);
		regMessage(MessageUnknownRequest.class);
	}
}
