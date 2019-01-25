package com.zerra.common.network.msg;

import com.zerra.common.Zerra;
import com.zerra.common.network.Message;
import com.zerra.common.network.MessageSide;
import com.zerra.common.world.World;
import com.zerra.server.ZerraServer;
import simplenet.Client;
import simplenet.packet.Packet;

import javax.annotation.Nonnull;

public class MessageConnect extends Message
{
	private Client client;

	public MessageConnect() {}

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
	protected void writeToPacket(Packet packet) {}

	@Override
	public void readFromClient(Client client)
	{
		this.client = client;
	}

	@Override
	public Message handle(Zerra zerra, World world)
	{
		//TODO: Player connection logic? e.g. spawn player and notify clients
		((ZerraServer) zerra).getConnectionManager().addClient(this.senderUuid, this.client);
		Zerra.logger().info("Player with UUID {} has joined the server", this.senderUuid);
		return null;
	}
}
