package com.zerra.client.state;

import com.zerra.client.gfx.Display;
import com.zerra.client.gfx.ui.Button;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * State: Settings
 * 
 * @author AndrewAlfazy
 */
public class SettingsState extends State
{

	Button back;

	public SettingsState()
	{
		super("settings");
		back = new Button(Display.getWidth() * 0.5f, Display.getHeight() - 120, 256, 32, true)
		{
			@Override
			public void mousePressed(double mouseX, double mouseY, int mouseButton)
			{
				if (this.isHovered() && mouseButton == 0)
				{
					StateManager.setActiveState(new MenuState());
				}
			}
		};
	}

	@Override
	public void update()
	{

	}

	@Override
	public void render(double mouseX, double mouseY, float partialTicks)
	{
		this.back.render(mouseX, mouseY, partialTicks);
	}

	@Override
	public void onMousePressed(double mouseX, double mouseY, int mouseButton)
	{
		this.back.mousePressed(mouseX, mouseY, mouseButton);
	}

	@Override
	public void onMouseReleased(double mouseX, double mouseY, int mouseButton)
	{
		this.back.mouseReleased(mouseX, mouseY, mouseButton);
	}
}