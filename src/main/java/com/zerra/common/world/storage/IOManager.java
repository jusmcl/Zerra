package com.zerra.common.world.storage;

import com.zerra.common.ArgsBuilder;
import com.zerra.common.CrashCodes;
import com.zerra.common.world.World;
import com.zerra.common.world.storage.plate.Plate;
import org.joml.Vector3i;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class IOManager {

    protected static File saves;

    protected static File instanceDir;

    public static File getSavesDirectory() {
        return saves;
    }

    public static File getInstanceDirectory() {
        return instanceDir;
    }

    public static void init(@Nonnull File folder) {
        File saves = new File(folder.getAbsolutePath() + "saves/");
        if (!saves.exists()) {
            try {
                saves.createNewFile();
            } catch (IOException e) {
                ArgsBuilder.LAUNCH.error(e.getMessage());
                System.exit(CrashCodes.IO_EXCEPTION);
            }
            setupDefaultFileSystem(saves);
        }
        IOManager.saves = saves;
        instanceDir = folder;
    }

    private static void setupDefaultFileSystem(File parent) {
        //TODO setup default folders inside the parent folder
    }


    public static class WorldStorageManager {

        private final World world;

        public WorldStorageManager(World world) {
            this.world = world;
        }

        public World getWorld() {
            return world;
        }

        @Nullable
        public Plate readPlateSafe(int layer, Vector3i pos){
            try{
                return readPlate(layer, pos);
            } catch (FileNotFoundException e){
                e.printStackTrace();
                return null;
            }
        }

        public Plate readPlate(int layer, Vector3i pos) throws FileNotFoundException {
            //TODO @Ocelot
            return null;
        }

        public boolean isPlateGenerated(int layer, Vector3i pos){
            //TODO @Ocelot
            return false;
        }

    }

}
