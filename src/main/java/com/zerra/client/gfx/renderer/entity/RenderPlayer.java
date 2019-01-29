package com.zerra.client.gfx.renderer.entity;

import com.zerra.client.RenderingManager;
import com.zerra.client.ZerraClient;
import com.zerra.client.gfx.texture.map.TextureMapSprite;
import com.zerra.client.util.ResourceLocation;
import com.zerra.client.world.ClientWorld;
import com.zerra.common.world.entity.EntityPlayer;

public class RenderPlayer implements EntityRender<EntityPlayer>
{
	private RenderingManager renderManager;
	private TextureMapSprite sprite;
	
	private static final String TEST_SKIN_URL = "https://raw.githubusercontent.com/Ocelot5836/storage/master/zerra/testPlayerSkin.png";

	public RenderPlayer()
	{
		this.renderManager = ZerraClient.getInstance().getRenderingManager();
		this.sprite = this.renderManager.getTextureManager().getTextureMap().getSprite(new ResourceLocation("textures/entity/playerTest.png"));
	}

	@Override
	public void render(EntityPlayer player, ClientWorld world, float depth, float partialTicks)
	{
		this.renderManager.getGuiRenderer().renderTexturedQuad(player.getXEntityPos(), player.getXEntityPos(), 32, 32, this.sprite);
	}

	@Override
	public void dispose()
	{

	}
}