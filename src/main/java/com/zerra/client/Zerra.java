package com.zerra.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.zerra.client.renderer.model.Model;
import com.zerra.client.renderer.texture.TextureManager;
import com.zerra.client.util.Loader;
import com.zerra.client.view.Display;

public class Zerra implements Runnable {

	private static final Logger LOGGER = LogManager.getLogger();

	private static Zerra instance;

	private ExecutorService pool;
	private ScheduledExecutorService loop;
	private TextureManager textureManager;

	// temp
	private Model model;

	public Zerra() {
		instance = this;
		this.pool = Executors.newCachedThreadPool();
		this.loop = Executors.newSingleThreadScheduledExecutor();
	}

	@Override
	public void run() {
		Display.createDisplay("Zerra", 1280, 720);
		try {
			this.init();
		} catch (Exception e) {
			LOGGER.fatal("Failed to initialize game", e);
			this.stop();
		}

		// temp
		while (!Display.isCloseRequested()) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			Display.update();

			GL30.glBindVertexArray(model.getVaoID());
			GL20.glEnableVertexAttribArray(0);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_FAN, 0, model.getVertexCount());
			GL20.glDisableVertexAttribArray(0);
			GL30.glBindVertexArray(0);
		}
		this.dispose();
	}

	private void init() throws Exception {
		GL11.glClearColor(1, 0, 1, 1);
		this.model = Loader.loadToVAO(new float[] { 0, 0, 0, 1, 1, 0, 1, 1 }, 2);
		this.textureManager = new TextureManager();
	}

	public void schedule(Runnable runnable) {
		Validate.notNull(runnable);
		this.pool.execute(runnable);
	}

	public void onKeyPressed(int keyCode) {
	}

	public void onKeyReleased(int keyCode) {
	}

	public void onMousePressed(double mouseX, double mouseY, int mouseButton) {
	}

	public void onMouseReleased(double mouseX, double mouseY, int mouseButton) {
	}

	public void onMouseScrolled(double mouseX, double mouseY, double yoffset) {
	}

	public void stop() {
		if (!Display.isCloseRequested()) {
			Display.close();
		}
	}

	public void dispose() {
		Display.destroy();
		pool.shutdown();
		loop.shutdown();
		instance = null;
	}

	public TextureManager getTextureManager() {
		return textureManager;
	}

	public static Logger logger() {
		return LOGGER;
	}

	public static Zerra getInstance() {
		return instance;
	}
}