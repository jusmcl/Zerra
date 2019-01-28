package com.zerra.client.gfx.ui;

import com.zerra.client.ZerraClient;
import com.zerra.client.util.ResourceLocation;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * 
 * UIComponent : Button
 * 
 * @author AndrewAlfazy
 */
public class Button extends UIComponent
{
	public static final ResourceLocation BUTTONS_0 = new ResourceLocation("textures/gui/buttons.png");
	public static final ResourceLocation BUTTONS_1 = new ResourceLocation("textures/gui/buttons2.png");

	private boolean hovered;
	private boolean sharpEdge;

	public Button(float x, float y, float width, float height, boolean isCenterdCoords)
	{
		this(x, y, width, height, isCenterdCoords, true);
	}

	public Button(float x, float y, float width, float height, boolean isCenterdCoords, boolean sharpEdge)
	{
		super(x, y, width, height, isCenterdCoords);
		this.hovered = false;
		this.sharpEdge = sharpEdge;
	}

	@Override
	public void render(double mouseX, double mouseY, float partialTicks)
	{
		this.hovered = mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.height;
		ZerraClient.getInstance().getRenderingManager().getTextureManager().bind(this.sharpEdge ? BUTTONS_1 : BUTTONS_0);
		ZerraClient.getInstance().getRenderingManager().getGuiRenderer().renderTexturedQuad(this.x, this.y, this.width, this.height, 0, this.hovered && ZerraClient.getInstance().getInputHandler().isMouseButtonPressed(0) ? 32 : this.hovered ? 16 : 0, 256, 16, 256, 256);
	}

	@Override
	public void mousePressed(double mouseX, double mouseY, int mouseButton)
	{
	}

	@Override
	public void mouseReleased(double mouseX, double mouseY, int mouseButton)
	{
	}

	public boolean isHovered()
	{
		return hovered;
	}
}