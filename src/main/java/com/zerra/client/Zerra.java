package com.zerra.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zerra.client.view.Display;

public class Zerra implements Runnable {

	private static final Logger LOGGER = LogManager.getLogger();

	private static Zerra instance;
	private ExecutorService pool;
	private ScheduledExecutorService loop;

	public Zerra() {
		instance = this;
		this.pool = Executors.newCachedThreadPool();
		this.loop = Executors.newSingleThreadScheduledExecutor();
	}

	public static void schedule(Runnable runnable) {
		Validate.notNull(runnable);
	}

	@Override
	public void run() {
		Display.createDisplay("Zerra", 1280, 720);
		while (!Display.isCloseRequested()) {
			Display.update();
		}
		System.out.println("client");
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
		instance = null;
		pool.shutdown();
		loop.shutdown();
	}

	public void dispose() {
	}

	public static Logger logger() {
		return LOGGER;
	}

	public static Zerra getInstance() {
		return instance;
	}
}