package com.zerra.common.event;

import com.zerra.client.ZerraClient;

public class Event
{

	private boolean consumed;
	private boolean cancelled;

	public void call()
	{
		ZerraClient.getInstance().getEventHandler().callEvent(this);
	}

	public void consume()
	{
		this.consumed = true;
	}

	public boolean isConsumed()
	{
		return this.consumed;
	}

	public void cancel()
	{
		this.cancelled = true;
	}

	public boolean isCancelled()
	{
		return cancelled;
	}
}