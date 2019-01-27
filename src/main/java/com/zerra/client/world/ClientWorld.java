package com.zerra.client.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import com.zerra.client.RenderingManager;
import com.zerra.client.ZerraClient;
import com.zerra.client.gfx.renderer.GuiRenderer;
import com.zerra.client.gfx.renderer.tile.TileRenderer;
import com.zerra.client.util.ResourceLocation;
import com.zerra.client.view.Display;
import com.zerra.common.world.World;
import com.zerra.common.world.entity.EntityPlayer;
import com.zerra.common.world.storage.IOManager.WorldStorageManager;

public class ClientWorld extends World
{
	private List<Pair<Integer, ResourceLocation>> tileIndexes;
	private Map<ResourceLocation, Integer> tileMapper;

	public ClientWorld(String name, Long seed)
	{
		super(name, seed, false);
		this.tileIndexes = new ArrayList<Pair<Integer, ResourceLocation>>();
		this.tileMapper = new HashMap<ResourceLocation, Integer>();

		// TODO remove this temp code and sync with server instead
		this.addEntity(new EntityPlayer(this));
	}

	@Override
	public void update()
	{
		// TODO: Update client world here.
	}

	public void render(float partialTicks)
	{
		ZerraClient zerra = ZerraClient.getInstance();
		RenderingManager renderManager = zerra.getRenderingManager();
		TileRenderer tileRenderer = renderManager.getTileRenderer();
		GuiRenderer guiRenderer = renderManager.getGuiRenderer();

		renderManager.getFbo().bindFrameBuffer();
		{
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			tileRenderer.renderTiles(renderManager.getCamera(), this, 0);
		}
		renderManager.getFbo().unbindFrameBuffer();

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, renderManager.getFbo().getColorTexture(0));
		guiRenderer.setProjectionMatrix(GuiRenderer.FBO_MATRIX);
		guiRenderer.renderTexturedQuad(0, 0, Display.getWidth(), Display.getHeight(), 0, 0, 1, 1, 1, 1);
		guiRenderer.restoreDefaultProjectionMatrix();
	}

	public void setTileIndexes(List<Pair<Integer, ResourceLocation>> tileIndexes)
	{
		this.tileIndexes.clear();
		this.tileIndexes.addAll(tileIndexes);
		this.tileMapper.clear();
		this.tileMapper.putAll(WorldStorageManager.createTileMapper(tileIndexes));
	}

	public List<Pair<Integer, ResourceLocation>> getTileIndexes()
	{
		return tileIndexes;
	}

	public Map<ResourceLocation, Integer> getTileMapper()
	{
		return tileMapper;
	}
}