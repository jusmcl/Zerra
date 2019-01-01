package com.zerra.client.renderer.model;

public class Model {

	private int vaoID;
	private int vertexCount;
	private boolean usesIndicies;

	public Model(int vaoID, int vertexCount, boolean usesIndicies) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		this.usesIndicies = usesIndicies;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
	public boolean usesIndicies() {
		return usesIndicies;
	}
}