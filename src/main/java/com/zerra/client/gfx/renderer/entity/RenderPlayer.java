package com.zerra.client.gfx.renderer.entity;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zerra.client.RenderingManager;
import com.zerra.client.ZerraClient;
import com.zerra.client.util.Loader;
import com.zerra.client.util.OnlineRequest;
import com.zerra.client.util.ResourceLocation;
import com.zerra.client.world.ClientWorld;
import com.zerra.common.Zerra;
import com.zerra.common.world.entity.EntityPlayer;

public class RenderPlayer implements EntityRender<EntityPlayer>
{
	public static final ResourceLocation DEFAULT_SKIN = new ResourceLocation("textures/entity/player.png");
	public static final String TEST_SKIN_URL = "https://raw.githubusercontent.com/Ocelot5836/storage/master/zerra/testPlayerSkin.png";

	private static final List<UUID> REQUESTED_IMAGES = Lists.<UUID>newArrayList();
	private static final Map<UUID, BufferedImage> LOADED_IMAGES = Maps.<UUID, BufferedImage>newConcurrentMap();
	private static final Map<UUID, ResourceLocation> TEXTURES = Maps.<UUID, ResourceLocation>newConcurrentMap();

	private RenderingManager renderManager;

	public RenderPlayer()
	{
		this.renderManager = ZerraClient.getInstance().getRenderingManager();
	}

	@Override
	public void render(EntityPlayer player, ClientWorld world, float depth, float partialTicks)
	{
		for (UUID id : LOADED_IMAGES.keySet())
		{
			ResourceLocation textureLocation = new ResourceLocation("skins/" + id);
			this.renderManager.getTextureManager().loadTexture(textureLocation, Loader.loadTexture(LOADED_IMAGES.get(id)));
			TEXTURES.put(id, textureLocation);
			LOADED_IMAGES.remove(id);
		}

		if (!TEXTURES.containsKey(player.getUUID()) && !REQUESTED_IMAGES.contains(player.getUUID()))
		{
			REQUESTED_IMAGES.add(player.getUUID());
			OnlineRequest.requestStream(TEST_SKIN_URL, stream -> this.loadImage(player.getUUID(), stream));
		}

		ResourceLocation location = TEXTURES.containsKey(player.getUUID()) ? TEXTURES.get(player.getUUID()) : DEFAULT_SKIN;
		this.renderManager.getTextureManager().bind(location);
		this.renderManager.getGuiRenderer().renderTexturedQuad(player.getXEntityPos(), player.getXEntityPos(), 32, 32, 0, 0, 32, 32, 256, 32);
	}

	@Override
	public void dispose()
	{
		for (UUID id : TEXTURES.keySet())
		{
			this.renderManager.getTextureManager().delete(TEXTURES.get(id));
		}
	}

	private void loadImage(UUID id, InputStream stream)
	{
		try
		{
			REQUESTED_IMAGES.remove(id);
			LOADED_IMAGES.put(id, ImageIO.read(stream));
		}
		catch (IOException e)
		{
			Zerra.logger().warn("Could not load player texture for \'" + id + "\'", e);
		}
	}
}