package com.mhova.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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
}
