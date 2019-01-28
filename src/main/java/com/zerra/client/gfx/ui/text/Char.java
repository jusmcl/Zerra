package com.zerra.client.gfx.ui.text;

public class Char
{
	float minTexcoordX, minTexcoordY, maxTexcoordX, maxTexcoordY, width, height, xoffset, yoffset, xadvance;

	Char(float x, float y, float width, float height, float xoffset, float yoffset, float xadvance, float textureWidth, float textureHeight)
	{
		this.minTexcoordX = x / textureWidth;
		this.minTexcoordY = y / textureHeight;
		this.maxTexcoordX = (x + width) / textureWidth;
		this.maxTexcoordY = (y + height) / textureHeight;
		this.width = width;
		this.height = height;
		this.xoffset = xoffset;
		this.yoffset = yoffset;
		this.xadvance = xadvance;
	}

}
