package com.zerra.common.network.msg;

import javax.annotation.Nonnull;

import com.zerra.common.Zerra;
import com.zerra.common.network.ClientWrapper;
import com.zerra.common.network.Message;
import com.zerra.common.network.MessageSide;
import com.zerra.common.world.World;

import simplenet.packet.Packet;

public class MessageConnect extends Message
{
	public MessageConnect()
	{
	}

	@Override
	public boolean includesSender()
	{
		return true;
	}

	@Nonnull
	@Override
	public MessageSide getReceivingSide()
	{
		return MessageSide.SERVER;
	}

	@Override
	protected void writeToPacket(Packet packet)
	{
	}

	@Override
	public void readFromClient(ClientWrapper client)
	{
	}

	@Override
	public Message handle(Zerra zerra, World world)
	{
		// TODO: Player connection logic? e.g. spawn player and notify clients
		Zerra.logger().info("Player with UUID {} has joined the server", this.senderUUID);
		return null;
	}
}
