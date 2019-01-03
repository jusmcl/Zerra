package com.zerra.client;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;
import org.joml.Vector3i;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.zerra.Launch;
import com.zerra.client.gfx.model.Model;
import com.zerra.client.gfx.renderer.tile.TileRenderer;
import com.zerra.client.gfx.shader.TestQuadShader;
import com.zerra.client.gfx.texture.TextureManager;
import com.zerra.client.gfx.texture.map.TextureMap;
import com.zerra.client.util.I18n;
import com.zerra.client.util.Loader;
import com.zerra.client.util.ResourceLocation;
import com.zerra.client.view.Camera;
import com.zerra.client.view.Display;
import com.zerra.common.world.World;
import com.zerra.common.world.tile.Tile;
import com.zerra.common.world.tile.Tiles;

public class Zerra implements Runnable {

	private static final Logger LOGGER = LogManager.getLogger(Launch.NAME);

	private static Zerra instance;

	private ExecutorService pool;
	private ScheduledExecutorService loop;
	private TextureManager textureManager;
	private TextureMap textureMap;
	private TileRenderer tileRenderer;
	private Camera camera;

	// temp
	private Model model;
	private TestQuadShader shader;
	private World world;

	public Zerra() {
		instance = this;
		this.pool = Executors.newCachedThreadPool();
		this.loop = Executors.newSingleThreadScheduledExecutor();
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

		this.world.getLayer(0).getPlate(new Vector3i(1, 0, 0));
		while (!Display.isCloseRequested()) {
			Display.update();
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			this.render();
		}
		this.dispose();
	}
	
	private void update() {
		this.camera.update();
	}
	
	private void render() {
		// temp
		{
			this.textureManager.bind(this.textureMap.getLocation());
			this.shader.start();
			GL30.glBindVertexArray(this.model.getVaoID());
			GL20.glEnableVertexAttribArray(0);
			GL11.glDrawElements(GL11.GL_TRIANGLES, this.model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			GL20.glDisableVertexAttribArray(0);
			GL30.glBindVertexArray(0);
			this.shader.stop();
		}
		this.tileRenderer.renderTiles(this.camera, this.world, 0);
	}

	private void init() throws Throwable {
		GL11.glClearColor(0, 0, 0, 1);
		I18n.setLanguage(new Locale("en", "us"));
		Tiles.registerTiles();
		this.textureManager = new TextureManager();
		this.textureMap = new TextureMap(new ResourceLocation("atlas"), this.textureManager);
		Tile[] tiles = Tiles.getTiles();
		for (Tile tile : tiles) {
			this.textureMap.register(tile.getTexture());
		}
		this.textureMap.stitch();

		this.model = Loader.loadToVAO(new float[] { 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0 }, new int[] { 0, 1, 3, 3, 1, 2 }, 3);
		this.shader = new TestQuadShader();
		this.shader.start();
		this.shader.loadProjectionMatrix(new Matrix4f().ortho(0, 1, 1, 0, 0.3f, 1000.0f));
		this.shader.stop();
		this.world = new World();
		this.tileRenderer = new TileRenderer();
		this.camera = new Camera();
		this.loop.scheduleAtFixedRate(() -> this.update(), 0, 17, TimeUnit.MILLISECONDS);
	}

	public void schedule(Runnable runnable) {
		Validate.notNull(runnable);
		this.pool.execute(runnable);
	}

	public void onKeyPressed(int keyCode) {
		this.camera.onKeyPressed(keyCode);
	}

	public void onKeyReleased(int keyCode) {
		this.camera.onKeyReleased(keyCode);
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