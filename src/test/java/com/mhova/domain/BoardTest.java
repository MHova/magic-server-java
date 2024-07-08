package com.mhova.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoardTest {
	private Board classUnderTest;

	@BeforeEach
	void setup() {
		classUnderTest = new Board();
	}

	@Test
	void cardNotAtOrigin() {
		boolean retVal = classUnderTest.moveCard(
				UnorderedZone.BATTLEFIELD_1, UnorderedZone.EXILE_1, "whatever");
		assertFalse(retVal);
	}

	@Test
	void moveCard() {
		final String id = "123";
		final Card card = new Card(id);
		classUnderTest.hand2.put(id, card);
		boolean retVal = classUnderTest.moveCard(
				UnorderedZone.HAND_2, UnorderedZone.BATTLEFIELD_2, id);
		assertTrue(retVal);
		assertTrue(classUnderTest.hand2.isEmpty());
		assertEquals(card, classUnderTest.battlefield2.get(id));
	}
}
