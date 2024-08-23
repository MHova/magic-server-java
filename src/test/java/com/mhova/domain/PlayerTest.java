package com.mhova.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayerTest {
	private Player classUnderTest;
	private Library library;

	@BeforeEach
	void setup() {
		library = mock();
		classUnderTest = new Player("Alice", library, new LinkedHashMap<>(),
				new LinkedHashMap<>(), new LinkedHashMap<>(),
				new LinkedHashMap<>());
	}

	@Test
	void millX() {
		final Card one = new Card("1", UUID.randomUUID(), null);
		final Card two = new Card("2", UUID.randomUUID(), null);
		final Card three = new Card("3", UUID.randomUUID(), null);
		final Card four = new Card("4", UUID.randomUUID(), null);
		when(library.removeTopX(3)).thenReturn(List.of(two, three, four));
		classUnderTest.getGraveyard().put(one.cardId(), one);

		classUnderTest.millX(3);

		final Card[] graveyard = classUnderTest.getGraveyard().values()
				.toArray(new Card[0]);
		assertEquals(4, graveyard.length);
		assertEquals(one, graveyard[0]);
		assertEquals(two, graveyard[1]);
		assertEquals(three, graveyard[2]);
		assertEquals(four, graveyard[3]);
	}

	@Test
	void exileTopX() {
		final Card one = new Card("1", UUID.randomUUID(), null);
		final Card two = new Card("2", UUID.randomUUID(), null);
		final Card three = new Card("3", UUID.randomUUID(), null);
		final Card four = new Card("4", UUID.randomUUID(), null);
		when(library.removeTopX(3)).thenReturn(List.of(two, three, four));
		classUnderTest.getExile().put(one.cardId(), one);

		classUnderTest.exileTopX(3);

		final Card[] exile = classUnderTest.getExile().values()
				.toArray(new Card[0]);
		assertEquals(4, exile.length);
		assertEquals(one, exile[0]);
		assertEquals(two, exile[1]);
		assertEquals(three, exile[2]);
		assertEquals(four, exile[3]);
	}

	@Test
	void drawX() {
		final Card one = new Card("1", UUID.randomUUID(), null);
		final Card two = new Card("2", UUID.randomUUID(), null);
		final Card three = new Card("3", UUID.randomUUID(), null);
		final Card four = new Card("4", UUID.randomUUID(), null);
		when(library.removeTopX(3)).thenReturn(List.of(two, three, four));
		classUnderTest.getHand().put(one.cardId(), one);

		classUnderTest.drawX(3);

		final Card[] hand = classUnderTest.getHand().values()
				.toArray(new Card[0]);
		assertEquals(4, hand.length);
		assertEquals(one, hand[0]);
		assertEquals(two, hand[1]);
		assertEquals(three, hand[2]);
		assertEquals(four, hand[3]);
	}
}
