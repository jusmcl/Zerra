package com.zerra.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zerra.Launch;

public class ZerraServer implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(Launch.NAME + " Server");

    @Override
    public void run() {
        System.out.println("server");
    }

    public static Logger logger() {
        return LOGGER;
    }
}