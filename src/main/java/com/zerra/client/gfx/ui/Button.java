package com.zerra.client.gfx.ui;

import org.lwjgl.glfw.GLFW;

import com.zerra.client.ZerraClient;
import com.zerra.client.gfx.renderer.Renderer;
import com.zerra.client.util.ResourceLocation;
import com.zerra.client.view.Display;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * UIComponent : Button
 * 
 * @author AndrewAlfazy
 */
public class Button extends UIComponent {

	private boolean isDown, isMouseHover, sharpEdge;

	private static final ResourceLocation BUTTONS_0 = new ResourceLocation("zerra:gui/buttons.png");
	private static final ResourceLocation BUTTONS_1 = new ResourceLocation("zerra:gui/buttons2.png");

	public Button(float x, float y, float width, float height, boolean isCenterdCoords) {
		super(x, y, width, height, isCenterdCoords);
		isDown = false;
		isMouseHover = false;
		sharpEdge = true;
	}

	public Button(float x, float y, float width, float height, boolean isCenterdCoords, boolean sharpEdge) {
		super(x, y, width, height, isCenterdCoords);
		isDown = false;
		isMouseHover = false;
		this.sharpEdge = sharpEdge;
	}

	@Override
	public void render() {
		double mouse_x = Display.getMouseX() / Renderer.SCALE;
		double mouse_y = Display.getMouseY() / Renderer.SCALE;

		if (mouse_x > x && mouse_x < x + width && mouse_y > y && mouse_y < y + height) {
			isMouseHover = true;
			whileHover();
			if (ZerraClient.getInstance().getInputHandler().isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
				isDown = true;
				whileDown();
			} else if (isDown == true) {
				isDown = false;
				onClick();
			}
		} else {
			isMouseHover = false;
			isDown = false;
		}

		if (sharpEdge)
			ZerraClient.getInstance().getRenderingManager().getTextureManager().bind(BUTTONS_1);
		else
			ZerraClient.getInstance().getRenderingManager().getTextureManager().bind(BUTTONS_0);

		if (isDown)
			ZerraClient.getInstance().getRenderingManager().getGuiRenderer().renderTexturedQuad(x, y, width, height, 0,
					32, 256, 16, 256, 256);
		else if (isMouseHover)
			ZerraClient.getInstance().getRenderingManager().getGuiRenderer().renderTexturedQuad(x, y, width, height, 0,
					16, 256, 16, 256, 256);
		else
			ZerraClient.getInstance().getRenderingManager().getGuiRenderer().renderTexturedQuad(x, y, width, height, 0,
					0, 256, 16, 256, 256);
	}

	public void whileDown() {
	}

	public void onClick() {
	}

	public void whileHover() {
	}

	public boolean isDown() {
		return isDown;
	}

	public boolean isMouseHover() {
		return isMouseHover;
	}

}
