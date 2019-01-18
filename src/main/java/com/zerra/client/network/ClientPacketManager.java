package com.zerra.client.network;

import simplenet.Client;
import simplenet.packet.Packet;

public class ClientPacketManager
{

	Client client;
	
	public ClientPacketManager()
	{
		client = new Client();
		
		this.createListeners();
	}
	
	public void connect()
	{
		client.connect("localhost", 43594);
	}

	public void createListeners()
	{
		client.onConnect(() -> {
		    System.out.println(client + " has connected to the server!");
		    
		    Packet.builder().putByte(0).putString("This client message has successfully gone through!").writeAndFlush(client);
		});
		
		client.preDisconnect(() -> System.out.println(client + " is about to disconnect from the server!"));

		client.postDisconnect(() -> System.out.println(client + " successfully disconnected from the server!"));
	}
	
	public void disconnect()
	{
		this.client.close();
	}
}
