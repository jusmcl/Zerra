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
		return (double)this.x / (double)this.atlasWidth;
	}
	
	public double getXMax() {
		return (double)(this.x + this.width) / (double)this.atlasWidth;
	}
	
	public double getYMin() {
		return (double)this.y / (double)this.atlasHeight;
	}
	
	public double getYMax() {
		return (double)(this.y + this.height) / (double)this.atlasHeight;
	}
	
	public int getAtlasWidth() {
		return atlasWidth;
	}
	
	public int getAtlasHeight() {
		return atlasHeight;
	}

	protected void setAtlasSize(int atlasWidth, int atlasHeight) {
		this.atlasWidth = atlasWidth;
		this.atlasHeight = atlasHeight;
	}
}