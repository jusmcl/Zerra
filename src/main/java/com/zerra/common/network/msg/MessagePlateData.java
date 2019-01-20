package com.zerra.common.network.msg;

import com.zerra.common.network.Message;
import com.zerra.common.network.Opcodes;
import com.zerra.common.world.storage.plate.Plate;

import simplenet.packet.Packet;

public class MessagePlateData implements Message
{

	private Plate plate;

	public MessagePlateData(Plate plate)
	{
		this.plate = plate;
	}

	@Override
	public Packet prepare()
	{
		return Packet.builder().putByte(Opcodes.PLATE_DATA).putBytes(plate.toBytes());
	}

}
