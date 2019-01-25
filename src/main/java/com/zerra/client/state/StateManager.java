package com.zerra.client.state;

import java.util.ArrayList;
import java.util.List;

import com.zerra.client.ZerraClient;
import com.zerra.common.network.msg.MessageDisconnect;

public class StateManager
{

	private List<State> states = new ArrayList<>();

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

	public List<State> getStates()
	{
		return states;
	}

	public static void stateSwitch(State prevState, State nextState)
	{
		// If we are leaving a world state... clean up the world.
		if (prevState instanceof WorldState)
		{
			ZerraClient.getInstance().getConnectionManager().sendToServer(new MessageDisconnect());
		} else if (nextState instanceof WorldLoadState)
		{
			// Prepare for world...
		}
	}
}
