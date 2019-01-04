package com.zerra.common.world.storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.joml.Vector2i;
import org.joml.Vector3i;

import com.zerra.client.util.ResourceLocation;
import com.zerra.common.world.World;
import com.zerra.common.world.storage.plate.Plate;
import com.zerra.common.world.tile.Tile;
import com.zerra.common.world.tile.Tiles;

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
		File saves = new File(folder, "saves");
		if (!saves.exists()) {
			saves.mkdirs();
			setupDefaultFileSystem(saves);
		}
		IOManager.saves = saves;
		instanceDir = folder;
	}

	private static void setupDefaultFileSystem(File parent) {
		// TODO setup default folders inside the parent folder
	}

	public static class WorldStorageManager {

		public static final short VERSION = 0;

		private final World world;

		private List<Pair<Integer, ResourceLocation>> tileIndexes;
		private Map<ResourceLocation, Integer> tileMapper;

		public WorldStorageManager(World world) {
			this.world = world;
			this.tileIndexes = new ArrayList<Pair<Integer, ResourceLocation>>();
			this.tileMapper = new HashMap<ResourceLocation, Integer>();

			try {
				this.readTileIndexes();
			} catch (IOException e) {
				e.printStackTrace();
				this.tileIndexes.clear();
			}
			this.populateTileIndexes();
		}

		@Nullable
		public void writePlateSafe(int layer, Plate plate) {
			try {
				this.writePlate(layer, plate);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Nullable
		public Plate readPlateSafe(int layer, Vector3i pos) {
			try {
				return this.readPlate(layer, pos);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		}

		private List<Pair<Integer, ResourceLocation>> loadTiles(File tiles) throws IOException {
			if (tiles.exists()) {
				List<Pair<Integer, ResourceLocation>> pairs = new ArrayList<Pair<Integer, ResourceLocation>>();
				DataInputStream is = new DataInputStream(new FileInputStream(tiles));
				while (is.available() > 0) {
					pairs.add(new ImmutablePair<Integer, ResourceLocation>((int) is.readShort(), new ResourceLocation(is.readUTF())));
				}
				is.close();
				return pairs;
			} else {
				return new ArrayList<Pair<Integer, ResourceLocation>>();
			}
		}

		private void readTileIndexes() throws IOException {
			/** Read tile index from file */
			File tileLookupFile = new File(IOManager.saves, this.world.getName() + "/tiles.dat");
			this.tileIndexes.addAll(this.loadTiles(tileLookupFile));
			FileUtils.touch(tileLookupFile);
		}

		private void populateTileIndexes() {
			/** Add missing tiles to map */
			for (Tile tile : Tiles.getTiles()) {
				int id = -1;
				for (int i = 0; i < this.tileIndexes.size(); i++) {
					if (this.tileIndexes.get(i).getRight().equals(tile.getRegistryID())) {
						id = this.tileIndexes.get(i).getLeft();
						this.tileMapper.put(tile.getRegistryID(), id);
						break;
					}
				}

				if (id == -1) {
					this.tileIndexes.add(new ImmutablePair<Integer, ResourceLocation>(this.tileIndexes.size(), tile.getRegistryID()));
					this.tileMapper.put(tile.getRegistryID(), this.tileIndexes.size() - 1);
				}
			}
		}

		public void writePlate(int layer, Plate plate) throws IOException {
			Vector3i pos = plate.getPlatePos();

			/** Create plate file */
			File plateFile = new File(IOManager.saves, this.world.getName() + "/plates-" + layer + "/" + pos.x + "-" + pos.y + "-" + pos.z + ".zpl");
			FileUtils.touch(plateFile);

			/** Write */
			File tileLookupFile = new File(IOManager.saves, this.world.getName() + "/tiles.dat");
			FileUtils.touch(tileLookupFile);

			{
				DataOutputStream os = new DataOutputStream(new FileOutputStream(tileLookupFile));
				/** Write all tiles to the lookup file */
				for (Pair<Integer, ResourceLocation> pair : this.tileIndexes) {
					os.writeShort(pair.getLeft());
					os.writeUTF(pair.getRight().toString());
				}
				os.close();
			}

			{
				DataOutputStream os = new DataOutputStream(new FileOutputStream(plateFile));
				/** Write version */
				os.writeShort(VERSION);
				/** Write plate tiles */
				for (int x = 0; x < Plate.SIZE; x++) {
					for (int z = 0; z < Plate.SIZE; z++) {
						Vector2i position = new Vector2i(x, z);
						os.writeShort(this.tileIndexes.get(this.tileMapper.get(plate.getTileAt(position).getRegistryID())).getLeft());
					}
				}
				os.close();
			}
		}

		public Plate readPlate(int layer, Vector3i pos) throws FileNotFoundException {
			// TODO @Ocelot
			return null;
		}

		public boolean isPlateGenerated(int layer, Vector3i pos) {
			return new File(IOManager.saves, this.world.getName() + "/plates-" + layer + "/" + pos.x + "-" + pos.y + "-" + pos.z + ".zpl").exists();
		}

		public World getWorld() {
			return world;
		}
	}
}