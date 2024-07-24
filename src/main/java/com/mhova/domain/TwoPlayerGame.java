package com.mhova.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.javatuples.Pair;

public class TwoPlayerGame {
	private final List<Step> steps = Arrays.asList(Step.values());

	private final int numPlayers = 2;

	// Tracks whose turn it is
	private final List<Players> active;

	// Tracks who has priority
	private List<Players> priority;

	// Number of players who have passed priority in succession
	private int numPlayersPassedPriority = 0;

	public TwoPlayerGame() {
		super();
		active = Arrays.asList(Players.values());
		resetPriority();
	}

	/**
	 * The current player with priority passes priority.
	 * 
	 * @return The next player to receive priority. Empty if all players have
	 *         passed in succession.
	 */
	public Optional<Players> passPriority() {
		Collections.rotate(priority, -1);
		numPlayersPassedPriority++;

		if(numPlayersPassedPriority >= numPlayers) {
			numPlayersPassedPriority = 0;
			return Optional.empty();
		}

		return Optional.of(priority.get(0));
	}

	/**
	 * Advance to the next step of the turn, advancing to the next turn if
	 * necessary.
	 * 
	 * @return the active player and the current step
	 */
	public Pair<Players, Step> advanceStep() {
		Collections.rotate(steps, -1);

		// advance the turn
		if(steps.get(0) == Step.UNTAP) {
			Collections.rotate(active, -1);
		}

		resetPriority();

		return Pair.with(active.get(0), steps.get(0));
	}

	/**
	 * Give priority to the active player
	 */
	private void resetPriority() {
		priority = new ArrayList<>(active);
	}

	public List<Players> getActive() {
		return active;
	}

	public List<Players> getPriority() {
		return priority;
	}
}
