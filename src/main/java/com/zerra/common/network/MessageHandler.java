package com.zerra.common.network;

import com.zerra.common.util.MiscUtils;
import com.zerra.common.world.World;

public interface MessageHandler<T extends Message>
{
	static <H extends MessageHandler<? extends Message>> H create(Class<H> handlerClass)
	{
		return MiscUtils.createNewInstance(handlerClass);
	}

	void handleMessage(T message, World world);
}
