package com.zerra.common.world.data;

import com.zerra.common.util.UBObjectWrapper;
import org.joml.Vector2i;
import org.joml.Vector2ic;

import javax.annotation.Nonnull;

public class ZerraWorldData extends WorldData
{
	public Long seed;
	public Vector2ic spawnPoint = new Vector2i();

	public ZerraWorldData(String name)
	{
		super(name);
	}

	@Nonnull
	@Override
	public UBObjectWrapper writeToUBO(@Nonnull UBObjectWrapper ubo)
	{
		if (this.seed != null)
		{
			ubo.setLong("seed", this.seed);
		}

		UBObjectWrapper spawnObject = new UBObjectWrapper();
		spawnObject.setInt("x", spawnPoint.x());
		spawnObject.setInt("y", spawnPoint.y());
		ubo.setUBObject("spawn", spawnObject);
		return super.writeToUBO(ubo);
	}

	@Override
	public void readFromUBO(@Nonnull UBObjectWrapper ubo)
	{
		this.seed = ubo.getLong("seed");

		UBObjectWrapper spawnObject = ubo.getUBObjectWrapped("spawn");
		int x = 0;
		int y = 0;
		if (spawnObject != null)
		{
			x = spawnObject.getInt("x");
			y = spawnObject.getInt("y");
		}
		this.spawnPoint = new Vector2i(x, y);

		super.readFromUBO(ubo);
	}
}
