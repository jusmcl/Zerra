package com.zerra.client.gfx.renderer.entity;

import com.zerra.common.world.entity.Entity;

public interface EntityRender<T extends Entity>
{
	void render(T entity, float depth, float partialTicks);

	void dispose();
}