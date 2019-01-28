package com.zerra.client.gfx.renderer.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.zerra.client.view.ICamera;
import com.zerra.client.world.ClientWorld;
import com.zerra.common.world.entity.Entity;

public class EntityRenderer
{
	private static final Map<Class<? extends Entity>, EntityRender<? extends Entity>> ENTITY_RENDERS = Maps.<Class<? extends Entity>, EntityRender<? extends Entity>>newHashMap();

	private Map<Integer, Map<Class<? extends Entity>, List<Entity>>> renderEntities;

	public EntityRenderer()
	{
		this.renderEntities = new HashMap<Integer, Map<Class<? extends Entity>, List<Entity>>>();
	}

	public void add(Collection<Entity> entities)
	{
		for (Entity entity : entities)
		{
			this.add(entity);
		}
	}

	public void add(Entity... entities)
	{
		for (Entity entity : entities)
		{
			this.add(entity);
		}
	}

	public void add(Entity entity)
	{
		if (!this.renderEntities.containsKey(entity.getLayerId()))
		{
			this.renderEntities.put(entity.getLayerId(), new HashMap<Class<? extends Entity>, List<Entity>>());
		}
		Map<Class<? extends Entity>, List<Entity>> entityBatch = this.renderEntities.get(entity.getLayerId());
		if (!entityBatch.containsKey(entity.getClass()))
		{
			entityBatch.put(entity.getClass(), new ArrayList<Entity>());
		}
		entityBatch.get(entity.getClass()).add(entity);
	}

	@SuppressWarnings("unchecked")
	public void renderEntities(ClientWorld world, int layer, ICamera camera, float partialTicks)
	{
		Map<Class<? extends Entity>, List<Entity>> layerBatch = this.renderEntities.get(layer);
		if (layerBatch != null)
		{
			for (Class<? extends Entity> type : layerBatch.keySet())
			{
				List<Entity> entities = layerBatch.get(type);
				for (Entity entity : entities)
				{
					if (ENTITY_RENDERS.containsKey(type))
					{
						((EntityRender<Entity>) ENTITY_RENDERS.get(type)).render(entity, world, 0.0f, partialTicks);
					}
					else
					{
						// TODO have an alternate rendering perhaps
					}
				}
			}
		}
		this.renderEntities.clear();
	}

	public void dispose()
	{
		// TODO dispose of said alternate renderer
	}

	public static void disposeRenders()
	{
		for (Class<? extends Entity> entityClass : ENTITY_RENDERS.keySet())
		{
			ENTITY_RENDERS.get(entityClass).dispose();
		}
		ENTITY_RENDERS.clear();
	}

	public static void bindEntityRender(Class<? extends Entity> entityClass, EntityRender<? extends Entity> render)
	{
		ENTITY_RENDERS.put(entityClass, render);
	}
}