package com.zerra.client.gfx.renderer.entity;

import com.zerra.client.gfx.ICamera;
import com.zerra.client.world.ClientWorld;
import com.zerra.common.world.entity.Entity;

public interface EntityRender<T extends Entity>
{
	void render(T entity, ClientWorld world, ICamera camera, float depth, float partialTicks);

	void dispose();
}