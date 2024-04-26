package com.mhova.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TurnOrderTest {
	private List<Player> players;
	private TurnOrder turnOrder;
	final Player p1 = new Player("Alice");
	final Player p2 = new Player("Bob");
	final Player p3 = new Player("Claire");

	@BeforeEach
	void setup() {
		players = new ArrayList<>();
		players.add(p1);
		players.add(p2);
		players.add(p3);
		turnOrder = new TurnOrder(players);
	}

	@Test
	void constructor_normal() {
		assertEquals(Optional.of(p1), turnOrder.getCurrentPlayer());
	}

	@Test
	void constructor_empty() {
		final TurnOrder turnOrder = new TurnOrder(new ArrayList<>());
		assertEquals(Optional.empty(), turnOrder.getCurrentPlayer());
	}

	@Test
	void getCurrentPlayer_does_not_mutate() {
		assertEquals(Optional.of(p1), turnOrder.getCurrentPlayer());
		assertEquals(Optional.of(p1), turnOrder.getCurrentPlayer());
		assertEquals(Optional.of(p1), turnOrder.getCurrentPlayer());
	}

	@Test
	void advanceTurn() {
		assertEquals(Optional.of(p1), turnOrder.getCurrentPlayer());
		assertEquals(Optional.of(p2), turnOrder.advanceTurn());
		assertEquals(Optional.of(p2), turnOrder.getCurrentPlayer());
		assertEquals(Optional.of(p3), turnOrder.advanceTurn());
		assertEquals(Optional.of(p3), turnOrder.getCurrentPlayer());
		assertEquals(Optional.of(p1), turnOrder.advanceTurn());
		assertEquals(Optional.of(p1), turnOrder.getCurrentPlayer());
	}
}
