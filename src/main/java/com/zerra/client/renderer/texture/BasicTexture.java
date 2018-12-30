package com.zerra.client.renderer.texture;

public class BasicTexture implements ITexture {

	private int id;
	private int width;
	private int height;
	
	public BasicTexture(int id, int width, int height) {
		this.id = id;
		this.width = width;
		this.height = height;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}
}