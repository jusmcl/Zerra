package com.zerra.client.gfx.ui.text;

import java.util.HashMap;

import com.zerra.client.util.ResourceLocation;

public class Font
{
	HashMap<Character, Char> chars;
	ResourceLocation locationTexture;
	String name;
	float size, lineHeight, base;

	Font(int charsCount, ResourceLocation locationTexture, String name, float size, float lineHeight, float base)
	{
		chars = new HashMap<Character, Char>(charsCount);
		this.locationTexture = locationTexture;
		this.name = name;
		this.size = size;
		this.lineHeight = lineHeight;
		this.base = base;
	}

}
