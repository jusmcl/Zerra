package com.zerra.client.state;

public class StateManager
{

	private static State activeState;

	private static boolean reachedFirstState = false;

	public static State getActiveState()
	{
		return activeState;
	}

	public static void setActiveState(State state)
	{
		if (!StateManager.reachedFirstState)
		{
			StateManager.reachedFirstState = true;
		} else
		{
			StateManager.stateSwitch(activeState, state);
		}

		activeState = state;
	}

	public static void stateSwitch(State prevState, State nextState)
	{
		prevState.cleanState();
	}
}
