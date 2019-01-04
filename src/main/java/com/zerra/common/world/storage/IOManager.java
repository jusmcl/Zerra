package com.zerra.common.world.storage;

import com.zerra.common.ArgsBuilder;
import com.zerra.common.CrashCodes;

import javax.annotation.Nonnull;
import java.io.File;

public class IOManager {

    protected static File saves;

    protected static File instanceDir;

    public static File getSavesDirectory() {
        return saves;
    }

    public static File getInstanceDirectory() {
        return instanceDir;
    }

    public static void init(@Nonnull File folder){
        saves = new File(folder.getAbsolutePath() + "saves/");
        if (!saves.exists()) {
            ArgsBuilder.LAUNCH.error("The Folder " + saves.getAbsolutePath() + " doesn't exist!!");
            System.exit(CrashCodes.INVALID_ARGUMENT);
        }
        instanceDir = folder;
    }


}
