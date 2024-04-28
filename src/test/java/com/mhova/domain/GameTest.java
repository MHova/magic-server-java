package com.mhova.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameTest {
	private Game game;
	private TurnOrder mockTurnOrder;

	@BeforeEach
	void setup() {
		mockTurnOrder = mock();
		game = new Game(mockTurnOrder);
	}

	@Test
	void noPlayers() {
		when(mockTurnOrder.getCurrentPlayer()).thenReturn(Optional.empty());
		Optional<Pair<Player, Step>> ret = game.advanceStep();
		verify(mockTurnOrder, never()).advanceTurn();
		assertEquals(Optional.empty(), ret);
	}

	@Test
	void notEndOfTurn() {
		final Player alice = new Player("Alice");
		when(mockTurnOrder.getCurrentPlayer()).thenReturn(Optional.of(alice));
		Optional<Pair<Player, Step>> ret = game.advanceStep();
		verify(mockTurnOrder, never()).advanceTurn();
		assertEquals(Optional.of(Pair.with(alice, Step.UPKEEP)), ret);
	}
	
	@Test
	void endOfTurn() {
		final Player alice = new Player("Alice");
		final Player bob = new Player("Bob");
		Optional<Pair<Player, Step>> ret;
		when(mockTurnOrder.getCurrentPlayer()).thenReturn(Optional.of(alice));
		
		ret = game.advanceStep();
		verify(mockTurnOrder, never()).advanceTurn();
		assertEquals(Optional.of(Pair.with(alice, Step.UPKEEP)), ret);
		
		ret = game.advanceStep();
		verify(mockTurnOrder, never()).advanceTurn();
		assertEquals(Optional.of(Pair.with(alice, Step.DRAW)), ret);
		
		ret = game.advanceStep();
		verify(mockTurnOrder, never()).advanceTurn();
		assertEquals(Optional.of(Pair.with(alice, Step.MAIN_1)), ret);
		
		ret = game.advanceStep();
		verify(mockTurnOrder, never()).advanceTurn();
		assertEquals(Optional.of(Pair.with(alice, Step.BEGINNING_OF_COMBAT)), ret);
		
		ret = game.advanceStep();
		verify(mockTurnOrder, never()).advanceTurn();
		assertEquals(Optional.of(Pair.with(alice, Step.ATTACKERS)), ret);
		
		ret = game.advanceStep();
		verify(mockTurnOrder, never()).advanceTurn();
		assertEquals(Optional.of(Pair.with(alice, Step.BLOCKERS)), ret);
		
		ret = game.advanceStep();
		verify(mockTurnOrder, never()).advanceTurn();
		assertEquals(Optional.of(Pair.with(alice, Step.FIRST_STRIKE_DAMAGE)), ret);
		
		ret = game.advanceStep();
		verify(mockTurnOrder, never()).advanceTurn();
		assertEquals(Optional.of(Pair.with(alice, Step.DAMAGE)), ret);
		
		ret = game.advanceStep();
		verify(mockTurnOrder, never()).advanceTurn();
		assertEquals(Optional.of(Pair.with(alice, Step.END_OF_COMBAT)), ret);
		
		ret = game.advanceStep();
		verify(mockTurnOrder, never()).advanceTurn();
		assertEquals(Optional.of(Pair.with(alice, Step.MAIN_2)), ret);
		
		ret = game.advanceStep();
		verify(mockTurnOrder, never()).advanceTurn();
		assertEquals(Optional.of(Pair.with(alice, Step.END)), ret);
		
		ret = game.advanceStep();
		verify(mockTurnOrder, never()).advanceTurn();
		assertEquals(Optional.of(Pair.with(alice, Step.CLEANUP)), ret);
		
		when(mockTurnOrder.advanceTurn()).thenReturn(Optional.of(bob));
		ret = game.advanceStep();
		verify(mockTurnOrder, times(1)).advanceTurn();
		assertEquals(Optional.of(Pair.with(bob, Step.UNTAP)), ret);
	}
}
