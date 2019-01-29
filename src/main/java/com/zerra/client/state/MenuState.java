package com.zerra.client.state;

import com.zerra.client.ZerraClient;
import com.zerra.client.gfx.Display;
import com.zerra.client.gfx.renderer.Renderer;
import com.zerra.client.gfx.ui.Button;
import com.zerra.client.presence.PresenceBuilder;
import com.zerra.client.util.ResourceLocation;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * State: Main Menu
 * 
 * @author AndrewAlfazy
 */
public class MenuState extends State
{
	public static final ResourceLocation BACKGROUND = new ResourceLocation("textures/gui/backgrounds/background.png");

	private Button buttonPlay;
	private Button buttonSettings;
	private Button buttonExit;

	public MenuState()
	{
		super("menu");

		this.buttonPlay = new Button(Display.getWidth() / 2 - 256, Display.getHeight() / 2 - 16 + 200, 512, 32, false)
		{
			@Override
			public void mousePressed(double mouseX, double mouseY, int mouseButton)
			{
				if (this.isHovered() && mouseButton == 0)
				{
					StateManager.setActiveState(new WorldState());
				}
			}
		};

		this.buttonSettings = new Button(Display.getWidth() / 2 - 256, Display.getHeight() / 2 + 20 + 200, 512, 32, false)
		{
			@Override
			public void mousePressed(double mouseX, double mouseY, int mouseButton)
			{
				if (this.isHovered() && mouseButton == 0)
				{
					StateManager.setActiveState(new SettingsState());
				}
			}
		};

		this.buttonExit = new Button(Display.getWidth() / 2 - 256, Display.getHeight() / 2 + 56 + 200, 512, 32, false)
		{
			@Override
			public void mousePressed(double mouseX, double mouseY, int mouseButton)
			{
				if (this.isHovered() && mouseButton == 0)
				{
					ZerraClient.getInstance().stop();
				}
			}
		};
	}
	
	@Override
	public PresenceBuilder setupPresence()
	{
		return new PresenceBuilder().setDetails("In Menu").setLargeImage("zerra");
	}

	@Override
	public void update()
	{
		ZerraClient.getInstance().getRenderingManager().getCamera().update();
		ZerraClient.getInstance().getInputHandler().updateGamepad();
	}

	@Override
	public void render(double mouseX, double mouseY, float partialTicks)
	{
		ZerraClient.getInstance().getRenderingManager().getTextureManager().bind(BACKGROUND);
		ZerraClient.getInstance().getRenderingManager().getGuiRenderer().renderTexturedQuad(0, 0, Display.getWidth() / Renderer.SCALE, Display.getHeight() / Renderer.SCALE, 0, 0, 1920, 1080, 1920, 1080);

		this.buttonPlay.render(mouseX, mouseY, partialTicks);
		this.buttonSettings.render(mouseX, mouseY, partialTicks);
		this.buttonExit.render(mouseX, mouseY, partialTicks);
	}

	@Override
	public void onMousePressed(double mouseX, double mouseY, int mouseButton)
	{
		this.buttonPlay.mousePressed(mouseX, mouseY, mouseButton);
		this.buttonSettings.mousePressed(mouseX, mouseY, mouseButton);
		this.buttonExit.mousePressed(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onMouseReleased(double mouseX, double mouseY, int mouseButton)
	{
		this.buttonPlay.mouseReleased(mouseX, mouseY, mouseButton);
		this.buttonSettings.mouseReleased(mouseX, mouseY, mouseButton);
		this.buttonExit.mouseReleased(mouseX, mouseY, mouseButton);
	}
}
