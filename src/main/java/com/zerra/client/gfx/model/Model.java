package com.zerra.client.gfx.model;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * Represents a model in a virtual space.
 *
 * @author Ocelot5836
 */
public class Model
{

	private int vaoID;
	private int vertexCount;
	private boolean usesIndicies;

	public Model(int vaoID, int vertexCount, boolean usesIndicies)
	{
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		this.usesIndicies = usesIndicies;
	}

	/**
	 * @return The VaoID of the model.
	 */
	public int getVaoID()
	{
		return vaoID;
	}

	/**
	 * @return How many vertices the model has.
	 */
	public int getVertexCount()
	{
		return vertexCount;
	}

	/**
	 * @return Whether or not the model uses indicies.
	 */
	public boolean usesIndicies()
	{
		return usesIndicies;
	}
}