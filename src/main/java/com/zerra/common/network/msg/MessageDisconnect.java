package com.zerra.common.network.msg;

import com.zerra.common.Zerra;
import com.zerra.common.network.ClientWrapper;
import com.zerra.common.network.Message;
import com.zerra.common.network.MessageSide;
import com.zerra.common.world.World;
import com.zerra.server.ZerraServer;
import simplenet.packet.Packet;

import javax.annotation.Nonnull;

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
	public void readFromClient(ClientWrapper client)
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
