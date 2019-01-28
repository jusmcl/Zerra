package com.zerra.client.world;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.joml.Vector3i;
import org.joml.Vector3ic;
import org.lwjgl.opengl.GL11;

import com.zerra.client.RenderingManager;
import com.zerra.client.ZerraClient;
import com.zerra.client.gfx.renderer.GuiRenderer;
import com.zerra.client.gfx.renderer.entity.EntityRenderer;
import com.zerra.client.gfx.renderer.tile.TileRenderer;
import com.zerra.client.util.ResourceLocation;
import com.zerra.client.view.Display;
import com.zerra.common.world.World;
import com.zerra.common.world.entity.EntityPlayer;
import com.zerra.common.world.storage.IOManager.WorldStorageManager;
import com.zerra.common.world.storage.Layer;

public class ClientWorld extends World
{
	private List<Pair<Integer, ResourceLocation>> tileIndexes;
	private Map<ResourceLocation, Integer> tileMapper;
	private List<byte[]> awaitingPlates;

	public ClientWorld(String name)
	{
		this(name, null);
	}

	public ClientWorld(String name, Long seed)
	{
		super(name, seed);
		this.tileIndexes = new ArrayList<Pair<Integer, ResourceLocation>>();
		this.tileMapper = new HashMap<ResourceLocation, Integer>();
		this.awaitingPlates = new ArrayList<byte[]>();

		// TODO remove this temp code and sync with server instead
		this.addEntity(new EntityPlayer(this));
	}

	@Override
	public void update()
	{
		if (!this.tileIndexes.isEmpty() && !this.awaitingPlates.isEmpty())
		{
			for (int i = 0; i < this.awaitingPlates.size(); i++)
			{
				List<Pair<Integer, ResourceLocation>> tileIndexes = this.getTileIndexes();
				try (DataInputStream is = new DataInputStream(new ByteArrayInputStream(this.awaitingPlates.get(i))))
				{
					int layer = is.readInt();
					Vector3ic platePos = new Vector3i(is.readInt(), is.readInt(), is.readInt());
					this.getLayer(layer).addPlate(platePos, WorldStorageManager.readPlate(is, this.getLayer(layer), platePos, tileIndexes));
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				this.awaitingPlates.remove(i);
				i--;
			}
		}

		super.update();
	}

	@Override
	public boolean isServer()
	{
		return false;
	}

	public void render(float partialTicks)
	{
		ZerraClient zerra = ZerraClient.getInstance();
		RenderingManager renderManager = zerra.getRenderingManager();
		TileRenderer tileRenderer = renderManager.getTileRenderer();
		EntityRenderer entityRenderer = renderManager.getEntityRenderer();
		GuiRenderer guiRenderer = renderManager.getGuiRenderer();

		Layer layer = this.getLayer(0);
		entityRenderer.add(layer.getEntities());

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
		entityRenderer.renderEntities(this, 0, renderManager.getCamera(), partialTicks);

	}

	public void setTileIndexes(List<Pair<Integer, ResourceLocation>> tileIndexes)
	{
		this.tileIndexes.clear();
		this.tileIndexes.addAll(tileIndexes);
		this.tileMapper.clear();
		this.tileMapper.putAll(WorldStorageManager.createTileMapper(tileIndexes));
	}

	public void processPlate(byte[] bytes)
	{
		this.awaitingPlates.add(bytes);
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