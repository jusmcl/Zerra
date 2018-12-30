package com.zerra.client;

import com.zerra.client.view.WindowManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Zerra implements Runnable {

	//3 threads; 1 for networking, 1 for rendering, 1 for gameloop
	ExecutorService executorService = Executors.newFixedThreadPool(3);
	
	public Zerra() {
		
	}

	public static void schedule(Runnable genRunnable) {
		//TODO: implement
	}

	@Override
	public void run() {
		WindowManager.init();
		System.out.println("client");
	}
}