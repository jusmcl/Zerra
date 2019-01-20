package com.zerra.common.util;

public class AccountProcessor
{
	private String sessionTicket;

	public AccountProcessor(String sessionTicket)
	{
		this.sessionTicket = sessionTicket;
	}

	/**
	 * Process the account info here.
	 */
	public void process()
	{
		// TODO: Hook this up with Tebreca's website.
	}

	public String getSessionTicket()
	{
		return sessionTicket;
	}
}
