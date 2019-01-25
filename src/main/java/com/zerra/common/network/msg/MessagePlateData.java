package com.zerra.common.network.msg;

import com.zerra.common.Zerra;
import com.zerra.common.network.Message;
import com.zerra.common.network.MessageSide;
import com.zerra.common.world.World;
import com.zerra.common.world.storage.plate.Plate;
import simplenet.Client;
import simplenet.packet.Packet;

import javax.annotation.Nonnull;

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
	public void readFromClient(Client client)
	{
		final int[] num = new int[1];
		client.readInt(value -> num[0] = value);
		//TODO: Fix this... Plate#fromBytes is messed up
		//client.readBytes(num[0], bytes -> this.plate = Plate.);
	}

	@Override
	public Message handle(Zerra zerra, World world)
	{
		//TODO: handle message
		return null;
	}
}
