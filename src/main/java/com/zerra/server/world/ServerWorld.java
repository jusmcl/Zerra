package com.zerra.server.world;

import com.zerra.common.world.World;

public class ServerWorld extends World
{

	public ServerWorld(String name)
	{
		super(name);
	}

	public void update()
	{
		// TODO: Update server world here.
	}

	@Override
	public void stop()
	{
		this.storageManager.writeWorldDataSafe(null, this.worldData);
		for (int i = 0; i < this.layers.length; i++)
		{
			int layerId = i;
			this.pool.execute(() -> this.save(layerId));
		}
		super.stop();
	}
}