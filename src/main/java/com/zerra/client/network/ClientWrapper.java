package com.zerra.client.network;

import java.util.UUID;

import simplenet.Client;

public class ClientWrapper
{

	private UUID uuid;
	private Client client;

	public ClientWrapper(UUID uuid, Client client)
	{
		this.client = client;
		this.uuid = uuid;
	}

	public Client getClient()
	{
		return client;
	}

	public UUID getUuid()
	{
		return uuid;
	}
}
