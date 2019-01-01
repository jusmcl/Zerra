package com.zerra.common.world.storage;

import java.io.File;

public class IOManager {

    public static File saves;

    public static void init(File folder){
        saves = new File(saves.getAbsolutePath() + "saves/");
    }


}
