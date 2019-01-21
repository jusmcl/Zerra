package com.zerra.api.mod.example;

import com.zerra.api.mod.Mod;
import com.zerra.api.mod.ModInit;
import com.zerra.api.mod.info.ModInfo;
import com.zerra.api.mod.info.ModInfoBuilder;
import com.zerra.common.util.UBObjectWrapper;
import com.zerra.common.world.data.WorldData;
import com.zerra.common.world.data.WorldDataFactory;
import com.zerra.common.world.item.Item;

import javax.annotation.Nonnull;

public class ExampleMod implements Mod
{
	public static final int PROGRESS_MAX = 100;

	@Override
	public void init(ModInit modInit)
	{
		// Item example
		Item item = new Item("testItem");
		modInit.register(item);

		// WorldData example
		modInit.register(new WorldDataFactory<>("exampleWorldData", ExampleWorldData.class));
		modInit.register(new WorldDataFactory<>("perLayerExampleWorldData", ExampleWorldData.class)
				.setIsPerLayer().setLayerPredicate(layer -> layer == 1));

		for (int i = 0; i < PROGRESS_MAX; i++)
		{
			float progress = ((float) i / (float) PROGRESS_MAX) * 100F;
			this.getLogger().info("MOD 1 PROGRESS: " + progress + "%");
		}

		this.getLogger().info(this.getModInfo().getModName() + " has finished initialization.");
	}

	@Override
	public void postInit()
	{
	}

	@Override
	public ModInfo getModInfo()
	{
		return new ModInfoBuilder("examplemod", "Example Mod", "1.0", "0.0.4").setAuthors("Arpaesis").setCredits("Credits to the Zerra development team for making the game!")
				.setDependencies("exampledependency", "exampledependency2", "anotherexampledependency").setModDescription("A simple example mod.").setWebsiteURL("https://www.example.com").build();
	}

	public static class ExampleWorldData extends WorldData
	{
		private int number = 1;

		public ExampleWorldData(String registryName)
		{
			super(registryName);
		}

		public void setNumber(int number)
		{
			this.number = number;
		}

		public int getNumber()
		{
			return this.number;
		}

		@Nonnull
		@Override
		public UBObjectWrapper writeToUBO(@Nonnull UBObjectWrapper ubo)
		{
			ubo.setInt("number", this.number);
			return super.writeToUBO(ubo);
		}

		@Override
		public void readFromUBO(@Nonnull UBObjectWrapper ubo)
		{
			this.number = ubo.getIntSafe("number");
			super.readFromUBO(ubo);
		}
	}
}
