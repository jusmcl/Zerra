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

import com.zerra.Launch;
import com.zerra.client.gfx.model.Model;
import com.zerra.client.gfx.shader.TestQuadShader;
import com.zerra.client.gfx.texture.TextureManager;
import com.zerra.client.gfx.texture.map.TextureMap;
import com.zerra.client.util.I18n;
import com.zerra.client.util.Loader;
import com.zerra.client.util.ResourceLocation;
import com.zerra.client.view.Display;

public class Zerra implements Runnable {

	private static final Logger LOGGER = LogManager.getLogger(Launch.NAME);

	private static Zerra instance;

	private ExecutorService pool;
	private ScheduledExecutorService loop;
	private TextureManager textureManager;
	private TextureMap textureMap;

	// temp
	private Model model;
	private TestQuadShader shader;

	public Zerra() {
		instance = this;
		this.pool = Executors.newCachedThreadPool();
		this.loop = Executors.newSingleThreadScheduledExecutor();
		this.schedule(() -> I18n.setLanguage(new Locale("en", "us")));
	}

	@Override
	public void run() {
		Display.createDisplay(Launch.NAME + " v" + Launch.VERSION, 1280, 720);
		Display.setIcon(new ResourceLocation("icons/16.png"), new ResourceLocation("icons/32.png"));

		try {
			this.init();
		} catch (Throwable e) {
			LOGGER.fatal("Failed to initialize game", e);
			this.stop();
		}

		while (!Display.isCloseRequested()) {
			Display.update();
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

			// temp
			{
				this.textureManager.bind(this.textureMap.getLocation());
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

	private void init() throws Throwable {
		GL11.glClearColor(1, 0, 1, 1);
		this.textureManager = new TextureManager();
		this.textureMap = new TextureMap(new ResourceLocation("atlas"), this.textureManager);
		this.textureMap.register(new ResourceLocation("textures/crate.png"));
		this.textureMap.register(new ResourceLocation("textures/crate256.png"));
		this.textureMap.register(new ResourceLocation("textures/test_boots.png"));
		this.textureMap.register(new ResourceLocation("textures/test_sword.png"));
		this.textureMap.register(new ResourceLocation("textures/test.png"));
		this.textureMap.register(new ResourceLocation("textures/test4.png"));
		this.textureMap.stitch();

		this.model = Loader.loadToVAO(new float[] { 0, 1, 0, 0, 1, 1, 1, 0 }, 2);
		this.shader = new TestQuadShader();
		this.shader.start();
		this.shader.loadProjectionMatrix(new Matrix4f().ortho(0, 1, 1, 0, 0.3f, 1000.0f));
		this.shader.stop();
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
		LOGGER.info("Stopping!");
		if (!Display.isCloseRequested()) {
			Display.close();
		}
	}

	public void dispose() {
		long startTime = System.currentTimeMillis();
		Display.destroy();
		Loader.cleanUp();
		this.textureManager.dispose();
		this.pool.shutdown();
		this.loop.shutdown();
		instance = null;
		logger().info("Disposed of all resources in " + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds");
	}

	public TextureManager getTextureManager() {
		return textureManager;
	}

	public TextureMap getTextureMap() {
		return textureMap;
	}

	public static Logger logger() {
		return LOGGER;
	}

	public static Zerra getInstance() {
		return instance;
	}
}