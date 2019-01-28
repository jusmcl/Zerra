package com.zerra.client.gfx.shader;

import org.joml.Matrix4f;

import com.zerra.common.Reference;

public class TextShader extends ShaderProgram
{
	private int location_projectionMatrix;
	private int location_transformationMatrix;

	public TextShader()
	{
		super(Reference.DOMAIN, "text");
	}

	@Override
	protected void bindAttributes()
	{
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "texcoords");
	}

	@Override
	protected void getAllUniformLocations()
	{
		this.location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		this.location_transformationMatrix = super.getUniformLocation("transformationMatrix");
	}

	public void loadProjectionMatrix(Matrix4f projectionMatrix)
	{
		super.loadMatrix(this.location_projectionMatrix, projectionMatrix);
	}

	public void loadTransformationMatrix(Matrix4f transformationMatrix)
	{
		super.loadMatrix(this.location_transformationMatrix, transformationMatrix);
	}

}
