package com.zerra.common.world.storage;

import com.zerra.common.ArgsBuilder;
import com.zerra.common.CrashCodes;

import javax.annotation.Nonnull;
import java.io.File;

public class IOManager {

	private static File dataDir;
	private static File saves;

    public static void init(@Nonnull File folder){
        saves = new File(folder.getAbsolutePath() + "saves/");
        if (!saves.exists()) {
            ArgsBuilder.LAUNCH.error("The Folder " + saves.getAbsolutePath() + " doesn't exist!!");
            System.exit(CrashCodes.INVALID_ARGUMENT);
        }
        dataDir = folder;
    }

    public static File getInstanceDirectory() {
        return dataDir;
    }

    public static File getSavesDirectory() {
        return saves;
    }
}