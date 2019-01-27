package com.zerra.common.network.msg;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.joml.Vector3i;
import org.joml.Vector3ic;

import com.zerra.client.util.ResourceLocation;
import com.zerra.client.world.ClientWorld;
import com.zerra.common.Zerra;
import com.zerra.common.network.Message;
import com.zerra.common.network.MessageSide;
import com.zerra.common.world.World;
import com.zerra.common.world.storage.IOManager.WorldStorageManager;
import com.zerra.common.world.storage.plate.Plate;

import simplenet.Client;
import simplenet.packet.Packet;

public class MessagePlateData extends Message
{
	private Plate plate;
	private List<Pair<Integer, ResourceLocation>> tileIndexes;
	private Map<ResourceLocation, Integer> tileMapper;

	private byte[] bytes;

	public MessagePlateData()
	{
	}

	public MessagePlateData(Plate plate, List<Pair<Integer, ResourceLocation>> tileIndexes, Map<ResourceLocation, Integer> tileMapper)
	{
		this.plate = plate;
		this.tileIndexes = tileIndexes;
		this.tileMapper = tileMapper;
	}

	@Override
	public MessageSide getReceivingSide()
	{
		return MessageSide.CLIENT;
	}

	@Override
	protected void writeToPacket(Packet packet)
	{
		ByteArrayOutputStream byteOs = new ByteArrayOutputStream();
		try (DataOutputStream os = new DataOutputStream(byteOs))
		{
			os.writeInt(this.plate.getLayer().getLayerId());
			os.writeInt(this.plate.getPlatePos().x());
			os.writeInt(this.plate.getPlatePos().y());
			os.writeInt(this.plate.getPlatePos().z());
			WorldStorageManager.writePlate(os, this.plate, this.tileIndexes, this.tileMapper);
			os.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		byte[] bytes = byteOs.toByteArray();
		packet.putInt(bytes.length).putBytes(bytes);
	}

	@Override
	public void readFromClient(Client client)
	{
		client.readInt(value -> client.readBytes(value, bytes ->
		{
			this.bytes = bytes;
		}));
	}

	@Override
	public Message handle(Zerra zerra, World world)
	{
		List<Pair<Integer, ResourceLocation>> tileIndexes = ((ClientWorld) world).getTileIndexes();
		try (DataInputStream is = new DataInputStream(new ByteArrayInputStream(this.bytes)))
		{
			int layer = is.readInt();
			Vector3ic platePos = new Vector3i(is.readInt(), is.readInt(), is.readInt());
			((ClientWorld)world).getLayer(layer).addPlate(platePos, WorldStorageManager.readPlate(is, world.getLayer(layer), platePos, tileIndexes));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
