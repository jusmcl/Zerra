package com.zerra.client.gfx.shader;

import org.joml.Matrix4f;

import com.zerra.client.gfx.ICamera;
import com.zerra.client.gfx.light.Light;
import com.zerra.client.gfx.renderer.Renderer;
import com.zerra.client.util.Maths;
import com.zerra.common.Reference;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * Used in shading tiles.
 *
 * @author Ocelot5836
 */
//TODO: Document
public class TileShader extends ShaderProgram
{

	private int location_projectionMatrix;
	private int location_transformationMatrix;
	private int location_viewMatrix;

	public TileShader()
	{
		super(Reference.DOMAIN, "tile");
	}

	@Override
	protected void bindAttributes()
	{
		super.bindAttribute(0, "positions");
		super.bindAttribute(1, "textureCoords");
	}

	@Override
	protected void getAllUniformLocations()
	{
		this.location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		this.location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		this.location_viewMatrix = super.getUniformLocation("viewMatrix");
	}

	@Override
	protected void setCompileParameters()
	{
		super.setCompileParameter("@maxLights", Renderer.MAX_LIGHTS);
	}

	public void loadProjectionMatrix(Matrix4f projectionMatrix)
	{
		super.loadMatrix(this.location_projectionMatrix, projectionMatrix);
	}

	public void loadTransformationMatrix(Matrix4f transformationMatrix)
	{
		super.loadMatrix(this.location_transformationMatrix, transformationMatrix);
	}

	public void loadViewMatrix(ICamera camera)
	{
		super.loadMatrix(this.location_viewMatrix, Maths.createViewMatrix(camera));
	}

	public void loadLights(Light... lights)
	{
	}
}