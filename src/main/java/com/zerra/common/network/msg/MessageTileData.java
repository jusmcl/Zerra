package com.zerra.common.network.msg;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.zerra.client.util.ResourceLocation;
import com.zerra.client.world.ClientWorld;
import com.zerra.common.Zerra;
import com.zerra.common.network.Message;
import com.zerra.common.network.MessageSide;
import com.zerra.common.world.World;

import simplenet.Client;
import simplenet.packet.Packet;

public class MessageTileData extends Message
{
	private List<Pair<Integer, ResourceLocation>> tileIndexes;

	public MessageTileData()
	{
		this(new ArrayList<Pair<Integer, ResourceLocation>>());
	}

	public MessageTileData(List<Pair<Integer, ResourceLocation>> tileIndexes)
	{
		this.tileIndexes = tileIndexes;
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
			os.writeInt(this.tileIndexes.size());
			for (Pair<Integer, ResourceLocation> pair : this.tileIndexes)
			{
				os.writeInt(pair.getLeft());
				os.writeUTF(pair.getRight().toString());
			}
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
		client.readInt(value -> client.readBytes(value, (bytes) ->
		{
			try (DataInputStream is = new DataInputStream(new ByteArrayInputStream(bytes)))
			{
				int size = is.readInt();
				for (int i = 0; i < size; i++)
				{
					this.tileIndexes.add(new ImmutablePair<Integer, ResourceLocation>(is.readInt(), new ResourceLocation(is.readUTF())));
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}));
	}

	@Override
	public Message handle(Zerra zerra, World world)
	{
		((ClientWorld) world).setTileIndexes(this.tileIndexes);
		return null;
	}
}