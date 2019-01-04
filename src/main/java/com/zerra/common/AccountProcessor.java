package com.zerra.common;

import com.playfab.PlayFabClientModels;
import com.playfab.PlayFabSettings;
import com.zerra.common.util.JsonWrapper;

public class AccountProcessor
{

	private String id;
	
	public AccountProcessor(String id) {
		this.id = id;
		
		JsonWrapper data = new JsonWrapper("data.json");
			PlayFabSettings.TitleId = data.getString("databaseID");
		data.close();
	}
	
	/**
	 * Process the account info here.
	 */
	public void process() {
		PlayFabClientModels.LoginWithCustomIDRequest request = new PlayFabClientModels.LoginWithCustomIDRequest();
		request.CustomId = id;
		
		// Below, check whether or not the account has purchased the game. If false (isValidPurchase == false), exit the game.
		// This is dummy code. Change it as you see fit!
		boolean isValidPurchase = true;
		
		if(isValidPurchase == false) {
			System.exit(1);
		}
	}
}
