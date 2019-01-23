package com.zerra.common;

import com.zerra.common.network.Message;
import com.zerra.common.network.MessageHandler;
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

	private static <T extends Message> void regMessage(Class<T> message, Class<? extends MessageHandler<T>> handler)
	{
		Registries.registerMessage(Reference.DOMAIN, message, handler);
	}

	public static void init()
	{
		//TODO: Move all base game content initialisation into here

		//Messages
		regMessage(MessageBadRequest.class, MessageBadRequest.Handler.class);
		regMessage(MessageConnect.class, MessageConnect.Handler.class);
		regMessage(MessageDisconnect.class, MessageDisconnect.Handler.class);
		regMessage(MessageEntityMove.class, MessageEntityMove.Handler.class);
		regMessage(MessagePing.class, MessagePing.Handler.class);
		regMessage(MessagePlateData.class, MessagePlateData.Handler.class);
		regMessage(MessageUnknownRequest.class, MessageUnknownRequest.Handler.class);
	}
}
