package com.zerra.common.world.storage;

import javax.annotation.Nonnull;
import java.io.File;

public class IOManager {

    public static File saves;

    public static void init(@Nonnull File folder){
        saves = new File(folder.getAbsolutePath() + "saves/");
    }


}
