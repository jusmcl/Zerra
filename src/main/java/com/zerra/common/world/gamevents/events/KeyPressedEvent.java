package com.zerra.common.world.gamevents.events;

import com.zerra.common.world.gamevents.Event;

public class KeyPressedEvent extends Event {

	private int keyCode;
	
	public KeyPressedEvent(int keyCode) {
		this.keyCode = keyCode;
	}
	
	public int getKeyCode() {
		return keyCode;
	}
}
