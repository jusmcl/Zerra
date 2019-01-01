package com.zerra.client.gfx.shader;

import org.joml.Matrix4f;

import com.zerra.client.Zerra;

public class TestQuadShader extends ShaderProgram {

	private int location_projectionMatrix;

	public TestQuadShader() {
		super(Zerra.DOMAIN, "test/quad");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		this.location_projectionMatrix = super.getUniformLocation("projectionMatrix");
	}

	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(this.location_projectionMatrix, matrix);
	}
}