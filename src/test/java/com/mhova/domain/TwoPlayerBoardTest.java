package com.mhova.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedHashMap;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TwoPlayerBoardTest {
	private TwoPlayerBoard classUnderTest;
	private Player player1;
	private Player player2;
	private Library library1;
	private LinkedHashMap<String, Card> hand2;
	private LinkedHashMap<String, Card> battlefield1;
	private LinkedHashMap<String, Card> battlefield2;

	@BeforeEach
	void setup() {
		player1 = mock();
		player2 = mock();
		library1 = mock();
		battlefield1 = new LinkedHashMap<>();
		hand2 = new LinkedHashMap<>();
		battlefield2 = new LinkedHashMap<>();
		when(player1.getBattlefield()).thenReturn(battlefield1);
		when(player2.getBattlefield()).thenReturn(battlefield2);
		when(player2.getHand()).thenReturn(hand2);
		when(player1.getLibrary()).thenReturn(library1);
		classUnderTest = new TwoPlayerBoard(player1, player2);
	}

	@Test
	void cardNotAtOrigin() {
		boolean retVal = classUnderTest.moveCard(UnorderedZone.BATTLEFIELD_1,
				UnorderedZone.EXILE_1, "whatever");
		assertFalse(retVal);

		retVal = classUnderTest.moveCardToBottom(UnorderedZone.BATTLEFIELD_1,
				Players.PLAYER_1, "whatever");
		assertFalse(retVal);
	}

	@Test
	void moveCard() {
		final String id = "123";
		final Card card = new Card(id, null, UUID.randomUUID(), null);
		hand2.put(id, card);
		boolean retVal = classUnderTest.moveCard(UnorderedZone.HAND_2,
				UnorderedZone.BATTLEFIELD_2, id);
		assertTrue(retVal);
		assertTrue(hand2.isEmpty());
		assertEquals(card, battlefield2.get(id));
	}

	@Test
	void moveCardToBottom() {
		final String id = "123";
		final Card card = new Card(id, null, UUID.randomUUID(), null);
		battlefield1.put(id, card);
		boolean retVal = classUnderTest.moveCardToBottom(
				UnorderedZone.BATTLEFIELD_1, Players.PLAYER_1, id);
		assertTrue(retVal);
		assertTrue(battlefield1.isEmpty());
		verify(library1).putCardOnBottom(card);
	}

	@Test
	void tryToMoveNonExistentCardToBottom() {
		boolean retVal = classUnderTest.moveCardToBottom(
				UnorderedZone.BATTLEFIELD_1, Players.PLAYER_1, "nope");
		assertFalse(retVal);
		verify(library1, never()).putCardOnBottom(any());
	}

	@Test
	void moveCardToTop() {
		final String id = "123";
		final Card card = new Card(id, null, UUID.randomUUID(), null);
		battlefield1.put(id, card);
		boolean retVal = classUnderTest.moveCardToTop(
				UnorderedZone.BATTLEFIELD_1, Players.PLAYER_1, id);
		assertTrue(retVal);
		assertTrue(battlefield1.isEmpty());
		verify(library1).putCardOnTop(card);
	}

	@Test
	void tryToMoveNonExistentCardToTop() {
		boolean retVal = classUnderTest.moveCardToTop(
				UnorderedZone.BATTLEFIELD_1, Players.PLAYER_1, "nope");
		assertFalse(retVal);
		verify(library1, never()).putCardOnTop(any());
	}

	@Test
	void moveCardXFromTop() {
		final String id = "123";
		final Card card = new Card(id, null, UUID.randomUUID(), null);
		battlefield1.put(id, card);
		boolean retVal = classUnderTest.moveCardXFromTop(
				UnorderedZone.BATTLEFIELD_1, Players.PLAYER_1, 2, id);
		assertTrue(retVal);
		assertTrue(battlefield1.isEmpty());
		verify(library1).putCardXFromTop(2, card);
	}

	@Test
	void tryToMoveNonExistentCardXFromTop() {
		boolean retVal = classUnderTest.moveCardXFromTop(
				UnorderedZone.BATTLEFIELD_1, Players.PLAYER_1, 2, "nope");
		assertFalse(retVal);
		verify(library1, never()).putCardXFromTop(anyInt(), any());
	}
}
