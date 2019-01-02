package com.zerra.client.gfx.texture.map;

public class TextureMapSprite {

	private int x;
	private int y;
	private int width;
	private int height;
	private int atlasWidth;
	private int atlasHeight;

	protected TextureMapSprite(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.atlasWidth = 0;
		this.atlasHeight = 0;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public double getXMin() {
		return this.x / this.atlasWidth;
	}
	
	public double getXMax() {
		return (this.x + this.width) / this.atlasWidth;
	}
	
	public double getYMin() {
		return this.y / this.atlasHeight;
	}
	
	public double getYMax() {
		return (this.y + this.height) / this.atlasHeight;
	}

	protected void setAtlasSize(int atlasWidth, int atlasHeight) {
		this.atlasWidth = atlasWidth;
		this.atlasHeight = atlasHeight;
	}
}