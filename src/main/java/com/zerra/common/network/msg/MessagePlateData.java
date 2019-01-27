package com.zerra.common.network.msg;

import javax.annotation.Nonnull;

import com.zerra.common.Zerra;
import com.zerra.common.network.ClientWrapper;
import com.zerra.common.network.Message;
import com.zerra.common.network.MessageSide;
import com.zerra.common.world.World;
import com.zerra.common.world.storage.plate.Plate;

import simplenet.packet.Packet;

public class MessagePlateData extends Message
{
	private Plate plate;

	public MessagePlateData(Plate plate)
	{
		this.plate = plate;
	}

	@Nonnull
	@Override
	public MessageSide getReceivingSide()
	{
		return MessageSide.CLIENT;
	}

	@Override
	protected void writeToPacket(Packet packet)
	{
		byte[] bytes = plate.toBytes();
		packet.putInt(bytes.length).putBytes(bytes);
	}

	@Override
	public void readFromClient(ClientWrapper client)
	{
		int numBytes = client.readInt();
		// TODO: Fix this... Plate#fromBytes is messed up
		// client.readBytes(num[0], bytes -> this.plate = Plate.);
	}

	@Override
	public Message handle(Zerra zerra, World world)
	{
		// TODO: handle message
		return null;
	}
}
