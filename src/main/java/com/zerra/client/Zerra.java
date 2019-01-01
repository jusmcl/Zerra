package com.zerra.client;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.zerra.client.renderer.model.Model;
import com.zerra.client.renderer.shader.TestQuadShader;
import com.zerra.client.renderer.texture.TextureManager;
import com.zerra.client.util.I18n;
import com.zerra.client.util.Loader;
import com.zerra.client.util.ResourceLocation;
import com.zerra.client.view.Display;

public class Zerra implements Runnable {

	public static final String NAME = "Zerra";
	public static final String VERSION = "0.0.1";
	public static final String DOMAIN = "zerra";
	private static final Logger LOGGER = LogManager.getLogger(NAME);

	private static Zerra instance;

	private ExecutorService pool;
	private ScheduledExecutorService loop;
	private TextureManager textureManager;

	// temp
	private Model model;
	private TestQuadShader shader;
	private ResourceLocation test;

	public Zerra() {
		instance = this;
		this.pool = Executors.newCachedThreadPool();
		this.loop = Executors.newSingleThreadScheduledExecutor();
		this.schedule(() -> I18n.setLanguage(new Locale("en", "us")));
	}

	@Override
	public void run() {
		Display.createDisplay(NAME + " v" + VERSION, 1280, 720);
		Display.setIcon(new ResourceLocation("icons/16.png"), new ResourceLocation("icons/32.png"));

		try {
			this.init();
		} catch (Exception e) {
			LOGGER.fatal("Failed to initialize game", e);
			this.stop();
		}

		while (!Display.isCloseRequested()) {
			Display.update();
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

			// temp
			{
				this.textureManager.bind(this.test);
				this.shader.start();
				GL30.glBindVertexArray(this.model.getVaoID());
				GL20.glEnableVertexAttribArray(0);
				GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, this.model.getVertexCount());
				GL20.glDisableVertexAttribArray(0);
				GL30.glBindVertexArray(0);
				this.shader.stop();
			}
		}
		this.dispose();
	}

	private void init() throws Exception {
		GL11.glClearColor(1, 0, 1, 1);
		this.textureManager = new TextureManager();

		this.model = Loader.loadToVAO(new float[] { 0, 1, 0, 0, 1, 1, 1, 0 }, 2);
		this.shader = new TestQuadShader();
		this.shader.start();
		this.shader.loadProjectionMatrix(new Matrix4f().ortho(0, 1, 1, 0, 0.3f, 1000.0f));
		this.shader.stop();
		this.test = new ResourceLocation("textures/test.png");
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
		Loader.cleanUp();
		this.textureManager.dispose();
		this.pool.shutdown();
		this.loop.shutdown();
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