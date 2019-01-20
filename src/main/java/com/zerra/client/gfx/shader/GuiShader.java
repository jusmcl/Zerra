package com.zerra.client.gfx.shader;

import org.joml.Matrix4f;

import com.zerra.ClientLaunch;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * Used for shading in GUI rendering.
 *
 * @author Ocelot5836
 */
//TODO: Document
public class GuiShader extends ShaderProgram
{

	private int location_projectionMatrix;
	private int location_transformationMatrix;

	private int location_textureData;
	private int location_color;

	public GuiShader()
	{
		super(ClientLaunch.DOMAIN, "gui");
	}

	@Override
	protected void bindAttributes()
	{
		super.bindAttribute(0, "positions");
	}

	@Override
	protected void getAllUniformLocations()
	{
		this.location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		this.location_transformationMatrix = super.getUniformLocation("transformationMatrix");

		this.location_textureData = super.getUniformLocation("textureData");
		this.location_color = super.getUniformLocation("color");
	}

	public void loadProjectionMatrix(Matrix4f projectionMatrix)
	{
		super.loadMatrix(this.location_projectionMatrix, projectionMatrix);
	}

	public void loadTransformationMatrix(Matrix4f transformationMatrix)
	{
		super.loadMatrix(this.location_transformationMatrix, transformationMatrix);
	}

	public void setTextureCoords(float xMin, float yMin, float xMax, float yMax)
	{
		super.loadVector(this.location_textureData, xMin, yMin, xMax, yMax);
	}

	public void setColor(float red, float green, float blue, float alpha)
	{
		super.loadVector(this.location_color, red, green, blue, alpha);
	}
}