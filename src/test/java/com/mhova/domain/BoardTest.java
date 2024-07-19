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

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoardTest {
	private Board classUnderTest;
	private Library library1;
	private Library library2;

	@BeforeEach
	void setup() {
		library1 = mock();
		library2 = mock();
		classUnderTest = new Board(library1, Optional.of(library2));
	}

	@Test
	void cardNotAtOrigin() {
		boolean retVal = classUnderTest.moveCard(UnorderedZone.BATTLEFIELD_1,
				UnorderedZone.EXILE_1, "whatever");
		assertFalse(retVal);

		retVal = classUnderTest.moveCardToBottom(UnorderedZone.BATTLEFIELD_1,
				PlayerLibrary.LIBRARY_1, "whatever");
		assertFalse(retVal);
	}

	@Test
	void moveCard() {
		final String id = "123";
		final Card card = new Card(id);
		classUnderTest.hand2.put(id, card);
		boolean retVal = classUnderTest.moveCard(UnorderedZone.HAND_2,
				UnorderedZone.BATTLEFIELD_2, id);
		assertTrue(retVal);
		assertTrue(classUnderTest.hand2.isEmpty());
		assertEquals(card, classUnderTest.battlefield2.get(id));
	}

	@Test
	void moveCardToBottom() {
		final String id = "123";
		final Card card = new Card(id);
		classUnderTest.battlefield1.put(id, card);
		boolean retVal = classUnderTest.moveCardToBottom(
				UnorderedZone.BATTLEFIELD_1, PlayerLibrary.LIBRARY_1, id);
		assertTrue(retVal);
		assertTrue(classUnderTest.battlefield1.isEmpty());
		verify(library1).putCardOnBottom(card);
	}

	@Test
	void tryToMoveNonExistentCardToBottom() {
		boolean retVal = classUnderTest.moveCardToBottom(
				UnorderedZone.BATTLEFIELD_1, PlayerLibrary.LIBRARY_1, "nope");
		assertFalse(retVal);
		verify(library1, never()).putCardOnBottom(any());
	}

	@Test
	void moveCardToTop() {
		final String id = "123";
		final Card card = new Card(id);
		classUnderTest.battlefield1.put(id, card);
		boolean retVal = classUnderTest.moveCardToTop(
				UnorderedZone.BATTLEFIELD_1, PlayerLibrary.LIBRARY_1, id);
		assertTrue(retVal);
		assertTrue(classUnderTest.battlefield1.isEmpty());
		verify(library1).putCardOnTop(card);
	}

	@Test
	void tryToMoveNonExistentCardToTop() {
		boolean retVal = classUnderTest.moveCardToTop(
				UnorderedZone.BATTLEFIELD_1, PlayerLibrary.LIBRARY_1, "nope");
		assertFalse(retVal);
		verify(library1, never()).putCardOnTop(any());
	}

	@Test
	void moveCardXFromTop() {
		final String id = "123";
		final Card card = new Card(id);
		classUnderTest.battlefield1.put(id, card);
		boolean retVal = classUnderTest.moveCardXFromTop(
				UnorderedZone.BATTLEFIELD_1, PlayerLibrary.LIBRARY_1, 2, id);
		assertTrue(retVal);
		assertTrue(classUnderTest.battlefield1.isEmpty());
		verify(library1).putCardXFromTop(2, card);
	}

	@Test
	void tryToMoveNonExistentCardXFromTop() {
		boolean retVal = classUnderTest.moveCardXFromTop(
				UnorderedZone.BATTLEFIELD_1, PlayerLibrary.LIBRARY_1, 2,
				"nope");
		assertFalse(retVal);
		verify(library1, never()).putCardXFromTop(anyInt(), any());
	}

	@Test
	void millXPlayer1() {
		final Card one = new Card("1");
		final Card two = new Card("2");
		final Card three = new Card("3");
		final Card four = new Card("4");
		when(library1.removeTopX(3)).thenReturn(List.of(two, three, four));
		classUnderTest.graveyard1.put(one.id(), one);

		classUnderTest.millX(Players.PLAYER_1, 3);

		final Card[] graveyard = classUnderTest.graveyard1.values()
				.toArray(new Card[0]);
		assertEquals(4, graveyard.length);
		assertEquals(one, graveyard[0]);
		assertEquals(two, graveyard[1]);
		assertEquals(three, graveyard[2]);
		assertEquals(four, graveyard[3]);
	}

	@Test
	void millXPlayer2() {
		final Card one = new Card("1");
		final Card two = new Card("2");
		final Card three = new Card("3");
		final Card four = new Card("4");
		when(library2.removeTopX(3)).thenReturn(List.of(two, three, four));
		classUnderTest.graveyard2.put(one.id(), one);

		classUnderTest.millX(Players.PLAYER_2, 3);

		final Card[] graveyard = classUnderTest.graveyard2.values()
				.toArray(new Card[0]);
		assertEquals(4, graveyard.length);
		assertEquals(one, graveyard[0]);
		assertEquals(two, graveyard[1]);
		assertEquals(three, graveyard[2]);
		assertEquals(four, graveyard[3]);
	}

	@Test
	void exileTopXPlayer1() {
		final Card one = new Card("1");
		final Card two = new Card("2");
		final Card three = new Card("3");
		final Card four = new Card("4");
		when(library1.removeTopX(3)).thenReturn(List.of(two, three, four));
		classUnderTest.exile1.put(one.id(), one);

		classUnderTest.exileTopX(Players.PLAYER_1, 3);

		final Card[] exile = classUnderTest.exile1.values()
				.toArray(new Card[0]);
		assertEquals(4, exile.length);
		assertEquals(one, exile[0]);
		assertEquals(two, exile[1]);
		assertEquals(three, exile[2]);
		assertEquals(four, exile[3]);
	}

	@Test
	void exileTopXPlayer2() {
		final Card one = new Card("1");
		final Card two = new Card("2");
		final Card three = new Card("3");
		final Card four = new Card("4");
		when(library2.removeTopX(3)).thenReturn(List.of(two, three, four));
		classUnderTest.exile2.put(one.id(), one);

		classUnderTest.exileTopX(Players.PLAYER_2, 3);

		final Card[] exile = classUnderTest.exile2.values()
				.toArray(new Card[0]);
		assertEquals(4, exile.length);
		assertEquals(one, exile[0]);
		assertEquals(two, exile[1]);
		assertEquals(three, exile[2]);
		assertEquals(four, exile[3]);
	}
}
