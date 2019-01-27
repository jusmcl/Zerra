package com.zerra.client.gfx.renderer.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.zerra.client.view.ICamera;
import com.zerra.common.world.entity.Entity;

public class EntityRenderer
{
	private static final Map<Class<? extends Entity>, EntityRender<? extends Entity>> ENTITY_RENDERS = Maps.<Class<? extends Entity>, EntityRender<? extends Entity>>newHashMap();

	private Map<Integer, Map<String, List<Entity>>> renderEntities;

	public EntityRenderer()
	{
		this.renderEntities = new HashMap<Integer, Map<String, List<Entity>>>();
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
			this.renderEntities.put(entity.getLayerId(), new HashMap<String, List<Entity>>());
		}
		Map<String, List<Entity>> entityBatch = this.renderEntities.get(entity.getLayerId());
		if (!entityBatch.containsKey(entity.getRegistryName()))
		{
			entityBatch.put(entity.getRegistryName(), new ArrayList<Entity>());
		}
		entityBatch.get(entity.getRegistryName()).add(entity);
	}

	public void renderEntities(int layer, ICamera camera, float partialTicks)
	{
		if (!this.renderEntities.isEmpty())
		{
			System.out.println(this.renderEntities);
		}
		this.renderEntities.clear();
	}

	public static <T extends Entity> void bindEntityRender(Class<T> entityClass, EntityRender<T> render)
	{
		ENTITY_RENDERS.put(entityClass, render);
	}
}