package com.zerra.client.gfx.shader;

import org.joml.Matrix4f;

import com.zerra.Launch;
import com.zerra.client.util.Maths;
import com.zerra.client.view.ICamera;

public class GuiShader extends ShaderProgram {

	private int location_projectionMatrix;
	private int location_transformationMatrix;
	private int location_viewMatrix;

	private int location_textureCoords;
	private int location_color[];

	public GuiShader() {
		super(Launch.DOMAIN, "gui");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "positions");
	}

	@Override
	protected void getAllUniformLocations() {
		this.location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		this.location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		this.location_viewMatrix = super.getUniformLocation("viewMatrix");

		this.location_textureCoords = super.getUniformLocation("textureCoords");

		this.location_color = new int[4];
		for (int i = 0; i < this.location_color.length; i++) {
			this.location_color[i] = super.getUniformLocation("color[" + i + "]");
		}
	}

	public void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(this.location_projectionMatrix, projectionMatrix);
	}

	public void loadTransformationMatrix(Matrix4f transformationMatrix) {
		super.loadMatrix(this.location_transformationMatrix, transformationMatrix);
	}

	public void loadViewMatrix(ICamera camera) {
		super.loadMatrix(this.location_viewMatrix, Maths.createViewMatrix(camera));
	}

	public void setTextureCoords(float xMin, float yMin, float xMax, float yMax) {
		super.loadVector(this.location_textureCoords, xMin, yMin, xMax, yMax);
	}

	public void setColor(float red, float green, float blue, float alpha) {
		for (int i = 0; i < 4; i++) {
			this.setColor(i, red, green, blue, alpha);
		}
	}

	public void setColor(int vertex, float red, float green, float blue, float alpha) {
		if (vertex < 0 || vertex >= this.location_color.length)
			return;
		super.loadVector(this.location_color[vertex], red, green, blue, alpha);
	}
}