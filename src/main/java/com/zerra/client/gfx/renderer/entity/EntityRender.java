package com.zerra.client.gfx.renderer.entity;

public interface EntityRender<T>
{
	void render(T entity, float depth, float partialTicks);
	
	void dispose();
}