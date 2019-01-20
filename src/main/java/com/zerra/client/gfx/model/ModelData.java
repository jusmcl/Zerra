package com.zerra.client.gfx.model;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * A container for the data a model has.
 *
 * @author Ocelot5836
 */
@Deprecated
public class ModelData {

	private float[] vertices;
	private float[] textureCoords;
	private float[] normals;
	private int[] indices;
	private float furthestPoint;

	public ModelData(float[] vertices, float[] textureCoords, float[] normals, int[] indices, float furthestPoint) {
		this.vertices = vertices;
		this.textureCoords = textureCoords;
		this.normals = normals;
		this.indices = indices;
		this.furthestPoint = furthestPoint;
	}

	/**
	 * @return The vertices the model has.
	 */
	public float[] getVertices() {
		return vertices;
	}

	/**
	 * @return The texture coords.
	 */
	public float[] getTextureCoords() {
		return textureCoords;
	}

	/**
	 * @return The normals of the model.
	 */
	public float[] getNormals() {
		return normals;
	}

	/**
	 * @return The indices of the model.
	 */
	public int[] getIndices() {
		return indices;
	}

	/**
	 * @return The furthest point of the model.
	 */
	public float getFurthestPoint() {
		return furthestPoint;
	}
}