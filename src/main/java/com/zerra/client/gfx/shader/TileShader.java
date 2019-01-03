package com.zerra.client.gfx.shader;

import org.joml.Matrix4f;

import com.zerra.Launch;
import com.zerra.client.gfx.Light;
import com.zerra.client.gfx.renderer.Renderer;

public class TileShader extends ShaderProgram {

	private int location_projectionMatrix;
	private int location_transformationMatrix;
	private int location_viewMatrix;

	private int[] location_lightPositions;
	private int[] location_lightColors;
	private int[] location_lightBrightness;

	public TileShader() {
		super(Launch.DOMAIN, "tile");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "positions");
		super.bindAttribute(1, "textureCoords");
	}

	@Override
	protected void getAllUniformLocations() {
		this.location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		this.location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		this.location_viewMatrix = super.getUniformLocation("viewMatrix");

		this.location_lightPositions = new int[Renderer.MAX_LIGHTS];
		this.location_lightColors = new int[Renderer.MAX_LIGHTS];
		this.location_lightBrightness = new int[Renderer.MAX_LIGHTS];
		for (int i = 0; i < Renderer.MAX_LIGHTS; i++) {
			this.location_lightPositions[i] = super.getUniformLocation("lightPositions[" + i + "]");
			this.location_lightColors[i] = super.getUniformLocation("lightColors[" + i + "]");
			this.location_lightBrightness[i] = super.getUniformLocation("lightBrightness[" + i + "]");
		}
	}
	
	@Override
	protected void setCompileParameters() {
		super.setCompileParameter("@maxLights", Renderer.MAX_LIGHTS);
	}

	public void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(this.location_projectionMatrix, projectionMatrix);
	}

	public void loadTransformationMatrix(Matrix4f transformationMatrix) {
		super.loadMatrix(this.location_transformationMatrix, transformationMatrix);
	}

	public void loadViewMatrix(Matrix4f viewMatrix) {
		super.loadMatrix(this.location_viewMatrix, viewMatrix);
	}

	public void loadLights(Light... lights) {
		for (int i = 0; i < Renderer.MAX_LIGHTS; i++) {
			if (i >= lights.length) {
				super.loadVector(this.location_lightPositions[i], 0, 0);
				super.loadVector(this.location_lightColors[i], 0, 0, 0);
				super.loadFloat(this.location_lightBrightness[i], 0);
			} else {
				Light light = lights[i];
				super.loadVector(this.location_lightPositions[i], light.getPosition());
				super.loadVector(this.location_lightColors[i], light.getColor());
				super.loadFloat(this.location_lightBrightness[i], light.getBrightness());
			}
		}
	}
}