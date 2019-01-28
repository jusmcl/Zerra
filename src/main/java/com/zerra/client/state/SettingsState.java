package com.zerra.client.state;

import com.zerra.client.gfx.ui.Button;
import com.zerra.client.view.Display;

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
				if (this.isHovered() && mouseButton == 1)
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
		back.render(mouseX, mouseY, partialTicks);
	}
}