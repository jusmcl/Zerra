package com.zerra.common.network.msg;

import com.zerra.common.Zerra;
import com.zerra.common.network.ClientWrapper;
import com.zerra.common.network.Message;
import com.zerra.common.util.MiscUtils;
import com.zerra.common.world.World;
import simplenet.packet.Packet;

public class MessagePing extends Message
{
	private long snapshot;

	public MessagePing()
	{
		this(System.currentTimeMillis());
	}

	public MessagePing(long time)
	{
		this.snapshot = time;
	}

	@Override
	public boolean includesSender()
	{
		return true;
	}

	@Override
	protected void writeToPacket(Packet packet)
	{
		packet.putLong(this.snapshot);
	}

	@Override
	public void readFromClient(ClientWrapper client)
	{
		this.snapshot = client.readLong();
	}

	@Override
	public Message handle(Zerra zerra, World world)
	{
		if (zerra.isClient())
		{
			Zerra.logger().info("Ping: " + MiscUtils.millisSinceTime(this.snapshot));
		} else
		{
			// Send the time snapshot back to the client
			return new MessagePing(this.snapshot);
		}
		return null;
	}
}
