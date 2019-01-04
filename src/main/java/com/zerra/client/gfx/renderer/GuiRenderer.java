package com.zerra.client.gfx.renderer;

import com.zerra.client.gfx.model.Model;
import com.zerra.client.gfx.shader.GuiShader;
import com.zerra.client.util.Loader;

public class GuiRenderer {

	private static final Model QUAD = Loader.loadToVAO(new float[] { 0, 0, 1, 0, 0, 1, 1, 1 }, 2);

	private GuiShader texturedShader;

	public GuiRenderer() {
		this.texturedShader = new GuiShader();
	}
}