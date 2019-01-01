package com.zerra.client.renderer.shader;

import com.zerra.client.Zerra;

public class TestQuadShader extends ShaderProgram {

	public TestQuadShader() {
		super(Zerra.DOMAIN, "test/quad");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
	}
}