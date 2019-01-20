package com.zerra.client.state;

import org.joml.Vector2i;
import org.lwjgl.opengl.GL11;

public class GameLoadState extends State
{

	private Vector2i framePosition;
	private Vector2i frameSize;
	private Vector2i barSize;
	private int frameWidth;

	public GameLoadState(int windowWidth, int windowHeight, int loadingBarWidth, int loadingBarHeight, int frameWidth)
	{
		super("gameload");
		this.framePosition = new Vector2i((windowWidth - loadingBarWidth) / 2, (windowHeight - loadingBarHeight) / 2);
		this.frameSize = new Vector2i(loadingBarWidth, loadingBarHeight);

	}

	@Override
	public void update()
	{

	}

	@Override
	public void render()
	{
		// Color or loading bar frame
		GL11.glClearColor(0.5f, 0.5f, 0.5f, 1f);
		// Draw loading bar frame
		GL11.glColor3f(0f, 0f, 0f);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		GL11.glLineWidth(frameWidth);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(-0.5f, 0.5f);
		GL11.glVertex2f(0.5f, 0.5f);
		GL11.glVertex2f(0.5f, -0.5f);
		GL11.glVertex2f(-0.5f, -0.5f);
		GL11.glEnd();

	}
}
