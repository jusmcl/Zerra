package com.zerra.common.network.msg;

import javax.annotation.Nonnull;

import com.zerra.common.Zerra;
import com.zerra.common.network.Message;
import com.zerra.common.network.MessageSide;
import com.zerra.common.world.World;
import com.zerra.server.ZerraServer;

import simplenet.Client;
import simplenet.packet.Packet;

public class MessageDisconnect extends Message
{
	public MessageDisconnect()
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
	public void readFromClient(Client client)
	{
	}

	@Override
	public Message handle(Zerra zerra, World world)
	{
		// TODO: Player disconnection logic? e.g. remove player and notify clients
		((ZerraServer) zerra).getConnectionManager().closeClient(this.senderUUID);
		Zerra.logger().info("Player with UUID {} has left the server", this.senderUUID);

		// If an integrated server, then shut down the server
		if (!((ZerraServer) zerra).isCurrentlyRemote())
		{
			zerra.stop();
		}
		return null;
	}
}
