package com.mhova.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TurnOrder {
	private final List<Player> players;

	public TurnOrder(final List<Player> players) {
		super();
		this.players = new ArrayList<>(players);
	}

	public Optional<Player> getCurrentPlayer() {
		return players.stream().findFirst();
	}

	public Optional<Player> advanceTurn() {
		Collections.rotate(players, -1);
		return getCurrentPlayer();
	}
}
