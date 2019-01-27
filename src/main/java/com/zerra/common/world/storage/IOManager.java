package com.zerra.common.world.storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.joml.Vector2i;
import org.joml.Vector3ic;

import com.devsmart.ubjson.UBArray;
import com.devsmart.ubjson.UBObject;
import com.devsmart.ubjson.UBReader;
import com.devsmart.ubjson.UBValue;
import com.devsmart.ubjson.UBValueFactory;
import com.devsmart.ubjson.UBWriter;
import com.zerra.client.util.ResourceLocation;
import com.zerra.common.registry.Registries;
import com.zerra.common.util.UBObjectWrapper;
import com.zerra.common.world.World;
import com.zerra.common.world.data.WorldData;
import com.zerra.common.world.entity.Entity;
import com.zerra.common.world.storage.plate.Plate;
import com.zerra.common.world.tile.Tile;
import com.zerra.common.world.tile.Tiles;
import com.zerra.server.world.ServerWorld;

public class IOManager
{

	protected static File saves;
	public static File instanceDir;

	public static File getSavesDirectory()
	{
		return saves;
	}

	public static File getInstanceDirectory()
	{
		return instanceDir;
	}

	public static void init(@Nonnull File folder)
	{
		File saves = new File(folder, "saves");
		if (!saves.exists())
		{
			saves.mkdirs();
		}
		IOManager.saves = saves;
		instanceDir = folder;
	}

	public static class WorldStorageManager
	{

		private static final int VERSION = 2;

		private final ServerWorld world;
		private final File worldDir;

		private List<Pair<Integer, ResourceLocation>> tileIndexes;
		private Map<ResourceLocation, Integer> tileMapper;

		public WorldStorageManager(ServerWorld world)
		{
			this.world = world;
			this.worldDir = new File(IOManager.saves, world.getName());
			this.tileIndexes = new ArrayList<Pair<Integer, ResourceLocation>>();
			this.tileMapper = new HashMap<ResourceLocation, Integer>();

			this.worldDir.mkdirs();

			// Check save version
			int version = readVersion();
			if (version < VERSION)
			{
				String[] contents = this.worldDir.list();
				if (contents == null || contents.length <= 0)
				{
					writeVersion();
				}
				else
				{
					// TODO: Convert the world to the new version?
					// But for now we'll ignore until we have converters or another solution in
					// place
					this.world.logger().warn("This world save is an old version! Save version: {}, Game version: {}", version, VERSION);
				}
			}

			try
			{
				this.readTileIndexes();
			}
			catch (IOException e)
			{
				e.printStackTrace();
				this.tileIndexes.clear();
			}
			this.populateTileIndexes();
		}

		private void writeVersion()
		{
			File file = this.getVersionFile();
			try
			{
				// noinspection ResultOfMethodCallIgnored
				file.createNewFile();
			}
			catch (IOException e)
			{
				this.world.logger().error("Couldn't create new version file", e);
				return;
			}
			try (DataOutputStream is = new DataOutputStream(new FileOutputStream(file)))
			{
				is.writeInt(VERSION);
			}
			catch (IOException e)
			{
				this.world.logger().warn("Error writing to version file!", e);
			}
		}

		private int readVersion()
		{
			File file = this.getVersionFile();
			try (DataInputStream is = new DataInputStream(new FileInputStream(file)))
			{
				return is.readInt();
			}
			catch (IOException ignored)
			{
			}
			return -1;
		}

		public void writePlateSafe(int layer, Plate plate)
		{
			try
			{
				this.writePlate(layer, plate);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		@Nullable
		public Plate readPlateSafe(int layer, Vector3ic pos)
		{
			try
			{
				return this.readPlate(layer, pos);
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return null;
			}
		}

		public void writeEntitiesSafe(int layer, Vector3ic platePos, Set<Entity> entities)
		{
			try
			{
				this.writeEntities(layer, platePos, entities);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		public Set<Entity> readEntitiesSafe(int layer, Vector3ic platePos)
		{
			try
			{
				return this.readEntities(layer, platePos);
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return null;
			}
		}

		public void writeWorldDataSafe(@Nullable Integer layer, Map<String, WorldData> worldDataMap)
		{
			try
			{
				this.writeWorldData(layer, worldDataMap);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		public Map<String, WorldData> readWorldDataSafe(@Nullable Integer layer)
		{
			try
			{
				return this.readWorldData(layer);
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return null;
			}
		}

		private List<Pair<Integer, ResourceLocation>> loadTiles(File tiles) throws IOException
		{
			if (tiles.exists())
			{
				List<Pair<Integer, ResourceLocation>> pairs = new ArrayList<Pair<Integer, ResourceLocation>>();
				try (DataInputStream is = new DataInputStream(new FileInputStream(tiles)))
				{
					pairs.addAll(readTileIndexes(is));
					is.close();
				}
				catch (Exception e)
				{
					this.world.logger().warn("Could not load tile indexes from {}", tiles.getName());
				}
				return pairs;
			}
			else
			{
				return new ArrayList<Pair<Integer, ResourceLocation>>();
			}
		}

		private void readTileIndexes() throws IOException
		{
			// Read tile index from file
			File tileLookupFile = new File(IOManager.saves, this.world.getName() + "/tiles.dat");
			this.tileIndexes.addAll(this.loadTiles(tileLookupFile));
			FileUtils.touch(tileLookupFile);
		}

		private void writeTileIndexes() throws IOException
		{
			// Write tile lookup
			File tileLookupFile = new File(IOManager.saves, this.world.getName() + "/tiles.dat");
			FileUtils.touch(tileLookupFile);

			try (DataOutputStream os = new DataOutputStream(new FileOutputStream(tileLookupFile)))
			{
				writeTileIndexes(os, this.tileIndexes);
				os.close();
			}
		}

		private void populateTileIndexes()
		{
			// Add missing tiles to map
			for (Tile tile : Tiles.getTiles())
			{
				int id = -1;
				for (Pair<Integer, ResourceLocation> tileIndex : this.tileIndexes)
				{
					if (tileIndex.getRight().equals(tile.getRegistryID()))
					{
						id = tileIndex.getLeft();
						break;
					}
				}

				if (id == -1)
				{
					this.tileIndexes.add(new ImmutablePair<Integer, ResourceLocation>(this.tileIndexes.size(), tile.getRegistryID()));
				}
			}
			this.tileMapper.putAll(createTileMapper(this.tileIndexes));

			try
			{
				this.writeTileIndexes();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		public void writePlate(int layer, Plate plate) throws IOException
		{
			// Create plate file
			File plateFile = this.getPlateFile(layer, plate.getPlatePos());
			FileUtils.touch(plateFile);

			try (DataOutputStream os = new DataOutputStream(new FileOutputStream(plateFile)))
			{
				writePlate(os, plate, this.tileIndexes, this.tileMapper);
				os.close();
			}
		}

		@Nullable
		public Plate readPlate(int layer, Vector3ic platePos) throws IOException
		{
			// Create plate file
			File plateFile = this.getPlateFile(layer, platePos);
			if (!plateFile.exists())
			{
				return null;
			}

			try (DataInputStream is = new DataInputStream(new FileInputStream(plateFile)))
			{
				Plate plate = readPlate(is, this.world.getLayer(layer), platePos, this.tileIndexes);
				is.close();
				return plate;
			}
		}

		public void writeEntities(int layer, Vector3ic platePos, Set<Entity> entities) throws IOException
		{
			File file = getEntityFile(layer, platePos);
			if (entities.isEmpty())
			{
				if (file.exists() && !file.delete())
				{
					this.world.logger().warn("Couldn't delete the entity file {}", file.getAbsolutePath());
				}
				return;
			}
			FileUtils.touch(file);

			// Write entities to file
			writeStorablesToFile(file, entities);
		}

		public Set<Entity> readEntities(int layer, Vector3ic platePos) throws IOException
		{
			File file = getEntityFile(layer, platePos);
			if (!file.exists())
			{
				return Collections.emptySet();
			}

			// Read entities from file

			// Set<Entity> entities = readStorablesFromFile(file, wrapper ->
			// {
			// return null;
			// });
			// return entities;
			return Collections.emptySet();
		}

		public void writeWorldData(@Nullable Integer layer, Map<String, WorldData> worldDataMap) throws IOException
		{
			File file = getWorldDataFile(layer);
			if (worldDataMap.isEmpty())
			{
				if (file.exists() && !file.delete())
				{
					this.world.logger().warn("Couldn't delete the world data file {}", file.getAbsolutePath());
				}
				return;
			}
			FileUtils.touch(file);

			// Write world data to file
			writeStorablesToFile(file, worldDataMap.values());
		}

		public Map<String, WorldData> readWorldData(@Nullable Integer layer) throws IOException
		{
			File file = getWorldDataFile(layer);
			if (!file.exists())
			{
				return Collections.emptyMap();
			}

			// Read world data from file
			Set<WorldData> worldData = readStorablesFromFile(file, object ->
			{
				String name = object.getString("name");
				WorldData data = Registries.getNewInstanceFromFactory(name, WorldData.class);
				data.readFromUBO(object);
				return data;
			});
			Map<String, WorldData> worldDataMap = new HashMap<>();
			worldData.forEach(data -> worldDataMap.put(data.getRegistryName(), data));
			return worldDataMap;
		}

		public void backupPlate(int layer, Vector3ic pos)
		{
			try
			{
				// Look for the plate file to back up
				File plateFile = this.getPlateFile(layer, pos);
				if (plateFile.exists())
				{
					File backupPlateFile = new File(IOManager.saves, this.world.getName() + "/plates-" + layer + "-bak/" + pos.x() + "_" + pos.y() + "_" + pos.z() + ".zpl");
					FileUtils.touch(backupPlateFile);
					org.apache.commons.io.IOUtils.copy(new FileInputStream(plateFile), new FileOutputStream(backupPlateFile));
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		/**
		 * Writes objects that implement {@link Storable} to the given file
		 *
		 * @param file
		 *            File to write to
		 * @param storables
		 *            Objects to write
		 * @throws IOException
		 *             If issues writing to file
		 */
		private static void writeStorablesToFile(File file, Collection<? extends Storable> storables) throws IOException
		{
			Set<UBObject> objects = storables.stream().map(o -> o.writeToUBO(new UBObjectWrapper()).getWrappedUBObject()).collect(Collectors.toSet());
			UBArray array = UBValueFactory.createArray(objects);

			try (UBWriter writer = new UBWriter(new FileOutputStream(file)))
			{
				writer.writeArray(array);
			}
		}

		/**
		 * Reads objects that implement {@link Storable} from a given file that have been written by writeStorablesToFile
		 *
		 * @param file
		 *            File the read from
		 * @param readFunction
		 *            {@link Function} to read and parse the data
		 * @param <T>
		 *            {@link Storable} type
		 * @return {@link Set} of the collected objects
		 * @throws IOException
		 *             If issues reading the file
		 */
		private static <T extends Storable> Set<T> readStorablesFromFile(File file, Function<UBObjectWrapper, T> readFunction) throws IOException
		{
			UBValue contents;
			try (UBReader reader = new UBReader(new FileInputStream(file)))
			{
				contents = reader.read();
			}

			if (contents == null || !contents.isArray())
			{
				throw new IOException("The contents of the file " + file.getAbsolutePath() + " was not a UBArray!");
			}
			UBArray array = contents.asArray();
			Set<T> storables = new HashSet<>();
			for (int i = 0; i < array.size(); i++)
			{
				UBValue value = array.get(i);
				if (!value.isObject())
				{
					throw new RuntimeException("A value in this array (file: " + file.getAbsolutePath() + ") is not a UBObject!");
				}
				T storable = readFunction.apply(new UBObjectWrapper(value.asObject()));
				if (storable != null)
				{
					storables.add(storable);
				}
			}
			return storables;
		}

		public boolean isPlateGenerated(int layer, Vector3ic pos)
		{
			return this.getPlateFile(layer, pos).exists();
		}

		private File getLayerDir(int layer)
		{
			return new File(this.worldDir, "layer-" + layer);
		}

		private File getPlateFile(int layer, Vector3ic pos)
		{
			return new File(getLayerDir(layer), "plate_" + pos.x() + "_" + pos.y() + "_" + pos.z() + ".zpl");
		}

		private File getEntityFile(int layer, Vector3ic platePos)
		{
			return new File(getLayerDir(layer), "entities_" + platePos.x() + "_" + platePos.y() + "_" + platePos.z() + ".ubj");
		}

		private File getWorldDataFile(Integer layer)
		{
			File parent = layer == null ? this.worldDir : getLayerDir(layer);
			return new File(parent, "world_data.ubj");
		}

		private File getVersionFile()
		{
			return new File(this.worldDir, "version.zsv");
		}

		public World getWorld()
		{
			return world;
		}

		public List<Pair<Integer, ResourceLocation>> getTileIndexes()
		{
			return this.tileIndexes;
		}

		public Map<ResourceLocation, Integer> getTileMapper()
		{
			return this.tileMapper;
		}

		public static void writeTileIndexes(DataOutputStream os, List<Pair<Integer, ResourceLocation>> tileIndexes) throws IOException
		{
			// Write all tiles to the lookup file
			for (Pair<Integer, ResourceLocation> pair : tileIndexes)
			{
				os.writeShort(pair.getLeft());
				os.writeUTF(pair.getRight().toString());
			}
		}

		public static List<Pair<Integer, ResourceLocation>> readTileIndexes(DataInputStream is) throws IOException
		{
			List<Pair<Integer, ResourceLocation>> pairs = new ArrayList<Pair<Integer, ResourceLocation>>();
			while (is.available() > 0)
			{
				pairs.add(new ImmutablePair<Integer, ResourceLocation>((int) is.readShort(), new ResourceLocation(is.readUTF())));
			}
			return pairs;
		}

		public static void writePlate(DataOutputStream os, Plate plate, List<Pair<Integer, ResourceLocation>> tileIndexes, Map<ResourceLocation, Integer> tileMapper) throws IOException
		{
			// Write plate tiles
			for (int x = 0; x < Plate.SIZE; x++)
			{
				for (int z = 0; z < Plate.SIZE; z++)
				{
					Vector2i position = new Vector2i(x, z);
					os.writeShort(tileIndexes.get(tileMapper.get(plate.getTileAt(position).getRegistryID())).getLeft());
				}
			}
		}

		public static Plate readPlate(DataInputStream is, Layer layer, Vector3ic platePos, List<Pair<Integer, ResourceLocation>> tileIndexes)
		{
			// Read plate tiles
			Plate plate = new Plate(layer);
			for (int x = 0; x < Plate.SIZE; x++)
			{
				for (int z = 0; z < Plate.SIZE; z++)
				{
					Vector2i tilePos = new Vector2i(x, z);
					try
					{
						plate.setTileAt(tilePos, Tiles.byId(tileIndexes.get(is.readShort()).getRight()));
					}
					catch (Exception e)
					{
						plate.setTileAt(tilePos, Tile.NONE);
					}
				}
			}
			plate.setPlatePos(platePos);
			return plate;
		}

		public static Map<ResourceLocation, Integer> createTileMapper(List<Pair<Integer, ResourceLocation>> tileIndexes)
		{
			Map<ResourceLocation, Integer> tileMapper = new HashMap<ResourceLocation, Integer>();
			for (Pair<Integer, ResourceLocation> tileIndex : tileIndexes)
			{
				tileMapper.put(tileIndex.getRight(), tileIndex.getLeft());
			}
			return tileMapper;
		}
	}
}