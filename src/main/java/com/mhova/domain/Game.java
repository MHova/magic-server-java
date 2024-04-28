package com.mhova.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.javatuples.Pair;

public class Game {
	private final TurnOrder turnOrder;
	private final List<Step> steps;

	public Game(TurnOrder turnOrder) {
		super();
		this.turnOrder = turnOrder;
		this.steps = Arrays.asList(Step.values());
	}

	public Optional<Pair<Player, Step>> advanceStep() {
		return turnOrder.getCurrentPlayer().map(currentPlayer -> {
			Collections.rotate(steps, -1);

			Player newCurrentPlayer = currentPlayer;

			// advance the turn
			if (steps.get(0) == Step.UNTAP) {
				// calling get() like this isn't great, but it really is an
				// exceptional case if there is somehow no next player
				newCurrentPlayer = turnOrder.advanceTurn().get();
			}

			return Pair.with(newCurrentPlayer, steps.get(0));
		});
	}
}
