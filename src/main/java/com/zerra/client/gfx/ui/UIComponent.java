package com.zerra.client.gfx.ui;

import com.zerra.client.gfx.renderer.Renderer;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * Any UI Component must extends this. <br>
 * <br>
 * Stores Component position and size.
 * 
 * @author AndrewAlfazy
 */
public abstract class UIComponent {

	protected float x, y, width, height;

	UIComponent(float x, float y, float width, float height, boolean centerdCoordnates) {
		if (centerdCoordnates) {
			this.x = x / Renderer.SCALE - width * 0.5f / Renderer.SCALE;
			this.y = y / Renderer.SCALE - height * 0.5f / Renderer.SCALE;
		} else {
			this.x = x / Renderer.SCALE;
			this.y = y / Renderer.SCALE;
		}
		this.width = width / Renderer.SCALE;
		this.height = height / Renderer.SCALE;
	}

	public void setPosition(float x, float y, boolean centerdCoordnates) {
		if (centerdCoordnates) {
			this.x = x / Renderer.SCALE - width * 0.5f / Renderer.SCALE;
			this.y = y / Renderer.SCALE - height * 0.5f / Renderer.SCALE;
		} else {
			this.x = x / Renderer.SCALE;
			this.y = y / Renderer.SCALE;
		}
	}

	public void setSize(float width, float height) {
		this.width = width / Renderer.SCALE;
		this.height = height / Renderer.SCALE;
	}

	public abstract void render(double mouseX, double mouseY, float partialTicks);
	
	public abstract void mousePressed(double mouseX, double mouseY, int mouseButton);
	
	public abstract void mouseReleased(double mouseX, double mouseY, int mouseButton);
}
