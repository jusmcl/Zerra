package com.zerra.client.gfx.renderer.entity;

import com.zerra.client.RenderingManager;
import com.zerra.client.ZerraClient;
import com.zerra.client.gfx.texture.map.TextureMapSprite;
import com.zerra.client.util.ResourceLocation;
import com.zerra.common.world.entity.EntityPlayer;

public class RenderPlayer implements EntityRender<EntityPlayer>
{
	private RenderingManager renderManager;
	private TextureMapSprite sprite;

	public RenderPlayer()
	{
		this.renderManager = ZerraClient.getInstance().getRenderingManager();
		this.sprite = this.renderManager.getTextureManager().getTextureMap().getSprite(new ResourceLocation("textures/entity/playerTest.png"));
	}

	@Override
	public void render(EntityPlayer player, float depth, float partialTicks)
	{
		this.renderManager.getGuiRenderer().renderTexturedQuad(player.getXEntityPos(), player.getYEntityPos(), 500, 500, this.sprite);
		System.out.println(player);
	}

	@Override
	public void dispose()
	{

	}
}