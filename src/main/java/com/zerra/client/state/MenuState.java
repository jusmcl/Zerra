package com.zerra.client.state;

import com.zerra.client.ZerraClient;
import com.zerra.client.gfx.renderer.Renderer;
import com.zerra.client.gfx.ui.Button;
import com.zerra.client.util.ResourceLocation;
import com.zerra.client.view.Display;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * State: Main Menu
 * 
 * @author AndrewAlfazy
 */
public class MenuState extends State {
	ResourceLocation background = new ResourceLocation("zerra:gui/backgrounds/background.png");
	Button play_button, settings_buttons, quit_button;

	public MenuState() {
		super("menu");
		play_button = new Button(Display.getWidth() * 0.5f, Display.getHeight() - 232, 256, 32, true) {
			@Override
			public void onClick() {
				StateManager.setActiveState(new WorldState());
			}
		};
		settings_buttons = new Button(Display.getWidth() * 0.5f, Display.getHeight() - 184, 256, 32, true) {
			@Override
			public void onClick() {
				StateManager.setActiveState(new SettingsState());
			}
		};
		quit_button = new Button(Display.getWidth() * 0.5f, Display.getHeight() - 120, 256, 32, true) {
			@Override
			public void onClick() {
				Display.close();
			}
		};
	}

	@Override
	public void update() {
		ZerraClient.getInstance().getRenderingManager().getCamera().update();
		ZerraClient.getInstance().getInputHandler().updateGamepad();
	}

	@Override
	public void render(float partialTicks) {
		ZerraClient.getInstance().getRenderingManager().getTextureManager().bind(background);
		ZerraClient.getInstance().getRenderingManager().getGuiRenderer().renderTexturedQuad(0, 0,
				Display.getWidth() / Renderer.SCALE, Display.getHeight() / Renderer.SCALE, 0, 0, 1920, 1080, 1920,
				1080);
		play_button.render();
		settings_buttons.render();
		quit_button.render();
	}
}
