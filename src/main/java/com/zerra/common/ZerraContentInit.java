package com.zerra.common;

import com.zerra.client.gfx.renderer.entity.EntityRenderer;
import com.zerra.client.gfx.renderer.entity.RenderPlayer;
import com.zerra.client.gfx.texture.map.TextureMap;
import com.zerra.client.util.ResourceLocation;
import com.zerra.common.network.Message;
import com.zerra.common.network.msg.MessageBadRequest;
import com.zerra.common.network.msg.MessageConnect;
import com.zerra.common.network.msg.MessageDisconnect;
import com.zerra.common.network.msg.MessageEntityMove;
import com.zerra.common.network.msg.MessagePing;
import com.zerra.common.network.msg.MessagePlateData;
import com.zerra.common.network.msg.MessageReady;
import com.zerra.common.network.msg.MessageTileData;
import com.zerra.common.network.msg.MessageUnknownRequest;
import com.zerra.common.registry.Registries;
import com.zerra.common.registry.RegistryNameable;
import com.zerra.common.world.data.WorldDataFactory;
import com.zerra.common.world.data.ZerraWorldData;
import com.zerra.common.world.entity.EntityPlayer;
import com.zerra.common.world.tile.Tile;
import com.zerra.common.world.tile.Tiles;

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
		// TODO: Move all base game content initialisation into here

		// World Data
		reg(new WorldDataFactory<>("worlddata", ZerraWorldData.class));

		// Messages
		regMessage(MessageBadRequest.class);
		regMessage(MessageConnect.class);
		regMessage(MessageDisconnect.class);
		regMessage(MessageEntityMove.class);
		regMessage(MessagePing.class);
		regMessage(MessageReady.class);
		regMessage(MessageTileData.class);
		regMessage(MessagePlateData.class);
		regMessage(MessageUnknownRequest.class);
	}

	public static void initClient()
	{
		EntityRenderer.bindEntityRender(EntityPlayer.class, new RenderPlayer());
	}

	public static void registerTextureMapSprites(TextureMap textureMap)
	{
		Tile[] tiles = Tiles.getTiles();
		for (Tile tile : tiles)
		{
			textureMap.register(tile.getTexture());
		}

		textureMap.register(new ResourceLocation("textures/entity/playerTest.png"));
	}
}
