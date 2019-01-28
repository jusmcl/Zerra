package com.zerra.client.gfx.ui.text;

public class TextMesh
{
	int vao, vboVertices, vboTexcoords, verticesCount;
	float width, height;

	TextMesh(int vao, int vboVertices, int vboTexcoords)
	{
		this.vao = vao;
		this.vboVertices = vboVertices;
		this.vboTexcoords = vboTexcoords;
		this.verticesCount = 0;
		this.width = 0;
		this.height = 0;
	}

}
