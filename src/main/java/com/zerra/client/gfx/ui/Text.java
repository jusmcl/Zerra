package com.zerra.client.gfx.ui;

import com.zerra.client.ZerraClient;
import com.zerra.client.gfx.ui.text.TextMesh;
import com.zerra.client.util.ResourceLocation;

public class Text extends UIComponent
{

	TextMesh mesh;
	String text;
	float size;

	public Text(float x, float y, String text, float size, ResourceLocation font, boolean centerdCoordnates)
	{
		super(x, y, -1, -1, centerdCoordnates);
		mesh = ZerraClient.getInstance().getRenderingManager().getFontManager().generateTextMesh(text, size, font, false);
	}

	public Text(float x, float y, String text, float size, ResourceLocation font, boolean centerdCoordnates, boolean dynamicText)
	{
		super(x, y, -1, -1, centerdCoordnates);
		mesh = ZerraClient.getInstance().getRenderingManager().getFontManager().generateTextMesh(text, size, font, dynamicText);
	}

	@Override
	public void render(double mouseX, double mouseY, float partialTicks)
	{
		ZerraClient.getInstance().getRenderingManager().getFontManager().render(mesh);
	}

	@Override
	public void mousePressed(double mouseX, double mouseY, int mouseButton)
	{
	}

	@Override
	public void mouseReleased(double mouseX, double mouseY, int mouseButton)
	{
	}

}
