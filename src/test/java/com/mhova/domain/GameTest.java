package com.mhova.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameTest {
	private Game game;
	private Board board;
	private Player alice;
	private Player bob;

	@BeforeEach
	void setup() {
		board = new Board();
		alice = new Player("Alice");
		bob = new Player("Bob");
		game = new Game(alice, Optional.of(bob), board);
	}

	@Test
	void newGameWithTwoPlayers() {
		final List<Players> active = game.getActive();
		assertEquals(2, active.size());
		assertEquals(Players.PLAYER_1, active.get(0));
		assertEquals(Players.PLAYER_2, active.get(1));

		final List<Players> priority = game.getPriority();
		assertEquals(2, priority.size());
		assertEquals(Players.PLAYER_1, priority.get(0));
		assertEquals(Players.PLAYER_2, priority.get(1));
	}

	@Test
	void newGameWithOnePlayer() {
		game = new Game(alice, Optional.empty(), board);
		final List<Players> active = game.getActive();
		assertEquals(1, active.size());
		assertEquals(Players.PLAYER_1, active.get(0));

		final List<Players> priority = game.getPriority();
		assertEquals(1, priority.size());
		assertEquals(Players.PLAYER_1, priority.get(0));
	}
	
	@Test
	void advanceStep() {
		// Player 1's turn
		assertPlayer1HasPriority();
		Pair<Players, Step> retVal = game.advanceStep();
		assertEquals(Pair.with(Players.PLAYER_1, Step.UPKEEP), retVal);
		assertPlayer1HasPriority();
		retVal = game.advanceStep();
		assertEquals(Pair.with(Players.PLAYER_1, Step.DRAW), retVal);
		assertPlayer1HasPriority();
		retVal = game.advanceStep();
		assertEquals(Pair.with(Players.PLAYER_1, Step.MAIN_1), retVal);
		assertPlayer1HasPriority();
		retVal = game.advanceStep();
		assertEquals(Pair.with(Players.PLAYER_1, Step.BEGINNING_OF_COMBAT), retVal);
		assertPlayer1HasPriority();
		retVal = game.advanceStep();
		assertEquals(Pair.with(Players.PLAYER_1, Step.ATTACKERS), retVal);
		assertPlayer1HasPriority();
		retVal = game.advanceStep();
		assertEquals(Pair.with(Players.PLAYER_1, Step.BLOCKERS), retVal);
		assertPlayer1HasPriority();
		retVal = game.advanceStep();
		assertEquals(Pair.with(Players.PLAYER_1, Step.FIRST_STRIKE_DAMAGE), retVal);
		assertPlayer1HasPriority();
		retVal = game.advanceStep();
		assertEquals(Pair.with(Players.PLAYER_1, Step.DAMAGE), retVal);
		assertPlayer1HasPriority();
		retVal = game.advanceStep();
		assertEquals(Pair.with(Players.PLAYER_1, Step.END_OF_COMBAT), retVal);
		assertPlayer1HasPriority();
		retVal = game.advanceStep();
		assertEquals(Pair.with(Players.PLAYER_1, Step.MAIN_2), retVal);
		assertPlayer1HasPriority();
		retVal = game.advanceStep();
		assertEquals(Pair.with(Players.PLAYER_1, Step.END), retVal);
		assertPlayer1HasPriority();
		retVal = game.advanceStep();
		assertEquals(Pair.with(Players.PLAYER_1, Step.CLEANUP), retVal);
		assertPlayer1HasPriority();
		
		// Player 2's turn
		retVal = game.advanceStep();
		assertEquals(Pair.with(Players.PLAYER_2, Step.UNTAP), retVal);
		assertPlayer2HasPriority();
		retVal = game.advanceStep();
		assertEquals(Pair.with(Players.PLAYER_2, Step.UPKEEP), retVal);
		assertPlayer2HasPriority();
		retVal = game.advanceStep();
		assertEquals(Pair.with(Players.PLAYER_2, Step.DRAW), retVal);
		assertPlayer2HasPriority();
		retVal = game.advanceStep();
		assertEquals(Pair.with(Players.PLAYER_2, Step.MAIN_1), retVal);
		assertPlayer2HasPriority();
		retVal = game.advanceStep();
		assertEquals(Pair.with(Players.PLAYER_2, Step.BEGINNING_OF_COMBAT), retVal);
		assertPlayer2HasPriority();
		retVal = game.advanceStep();
		assertEquals(Pair.with(Players.PLAYER_2, Step.ATTACKERS), retVal);
		assertPlayer2HasPriority();
		retVal = game.advanceStep();
		assertEquals(Pair.with(Players.PLAYER_2, Step.BLOCKERS), retVal);
		assertPlayer2HasPriority();
		retVal = game.advanceStep();
		assertEquals(Pair.with(Players.PLAYER_2, Step.FIRST_STRIKE_DAMAGE), retVal);
		assertPlayer2HasPriority();
		retVal = game.advanceStep();
		assertEquals(Pair.with(Players.PLAYER_2, Step.DAMAGE), retVal);
		assertPlayer2HasPriority();
		retVal = game.advanceStep();
		assertEquals(Pair.with(Players.PLAYER_2, Step.END_OF_COMBAT), retVal);
		assertPlayer2HasPriority();
		retVal = game.advanceStep();
		assertEquals(Pair.with(Players.PLAYER_2, Step.MAIN_2), retVal);
		assertPlayer2HasPriority();
		retVal = game.advanceStep();
		assertEquals(Pair.with(Players.PLAYER_2, Step.END), retVal);
		assertPlayer2HasPriority();
		retVal = game.advanceStep();
		assertEquals(Pair.with(Players.PLAYER_2, Step.CLEANUP), retVal);
		assertPlayer2HasPriority();
		
		// Back to Player 1
		retVal = game.advanceStep();
		assertEquals(Pair.with(Players.PLAYER_1, Step.UNTAP), retVal);
		assertPlayer1HasPriority();
	}
	
	@Test
	void passPriority() {
		Optional<Players> retVal = game.passPriority();
		assertEquals(Optional.of(Players.PLAYER_2), retVal);
		retVal = game.passPriority();
		assertEquals(Optional.empty(), retVal);
	}
	
	@Test
	void resolveEmptyStack() {
		Optional<Card> retVal = game.resolveStack();
		List<Players> priority = game.getPriority();
		assertEquals(Optional.empty(), retVal);
		assertEquals(Players.PLAYER_1, priority.get(0));
		assertEquals(Players.PLAYER_2, priority.get(1));
	}
	
	@Test
	void resolveNonEmptyStack() {
		Card card = new Card("Dark Ritual");
		board.stack.push(card);
		Optional<Card> retVal = game.resolveStack();
		assertEquals(Optional.of(card), retVal);
		assertEquals(Players.PLAYER_1, game.getPriority().get(0));
		assertEquals(Players.PLAYER_2, game.getPriority().get(1));
	}
	
	@Test
	void resolveNonEmptyStackOnPlayer2sTurn() {
		game.advanceStep(); // UPKEEP
		game.advanceStep(); // DRAW
		game.advanceStep(); // MAIN 1
		game.advanceStep(); // BEGINNING OF COMBAT
		game.advanceStep(); // ATTACKERS
		game.advanceStep(); // BLOCKERS
		game.advanceStep(); // FIRST STRIKE DAMAGE
		game.advanceStep(); // REGULAR DAMAGE
		game.advanceStep(); // END OF COMBAT
		game.advanceStep(); // MAIN 2
		game.advanceStep(); // END
		game.advanceStep(); // CLEANUP
		game.advanceStep(); // PLAYER 2 UNTAP
		game.advanceStep(); // PLAYER 2 UPKEEP
		
		game.passPriority();
		assertEquals(Players.PLAYER_1, game.getPriority().get(0));
		
		Card card = new Card("Dark Ritual");
		board.stack.push(card);
		Optional<Card> retVal = game.resolveStack();
		assertEquals(Optional.of(card), retVal);
		assertEquals(Players.PLAYER_2, game.getPriority().get(0));
		assertEquals(Players.PLAYER_1, game.getPriority().get(1));
	}
	
	void assertPlayer1HasPriority() {
		final List<Players> priority = game.getPriority();
		assertEquals(2, priority.size());
		assertEquals(Players.PLAYER_1, priority.get(0));
	}
	
	void assertPlayer2HasPriority() {
		final List<Players> priority = game.getPriority();
		assertEquals(2, priority.size());
		assertEquals(Players.PLAYER_2, priority.get(0));
	}
}
