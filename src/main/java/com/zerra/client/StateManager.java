package com.zerra.client;

import java.util.ArrayList;
import java.util.List;

public class StateManager
{

	private List<State> states = new ArrayList<>();

	private static State activeState;

	public static State getActiveState()
	{
		return activeState;
	}

	public static void setActiveState(State state)
	{
		activeState = state;
	}

	public List<State> getStates()
	{
		return states;
	}
}
